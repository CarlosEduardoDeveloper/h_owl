package com.example.foundation.modules.quiz.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.Viveiro;
import com.example.foundation.modules.gamification.domain.enums.SaudeFloresta;
import com.example.foundation.modules.gamification.service.BiscoitoService;
import com.example.foundation.modules.gamification.service.FlorestaStreakService;
import com.example.foundation.modules.quiz.domain.Alternativa;
import com.example.foundation.modules.quiz.domain.Questao;
import com.example.foundation.modules.quiz.domain.Quiz;
import com.example.foundation.modules.quiz.domain.RespostaQuestao;
import com.example.foundation.modules.quiz.domain.TentativaQuiz;
import com.example.foundation.modules.quiz.dto.AlternativaJogarResponse;
import com.example.foundation.modules.quiz.dto.FinalizarTentativaQuizRequest;
import com.example.foundation.modules.quiz.dto.FinalizarTentativaQuizResponse;
import com.example.foundation.modules.quiz.dto.IniciarTentativaQuizResponse;
import com.example.foundation.modules.quiz.dto.QuestaoJogarResponse;
import com.example.foundation.modules.quiz.dto.QuizJogarResponse;
import com.example.foundation.modules.quiz.dto.RespostaQuizItemRequest;
import com.example.foundation.modules.quiz.repository.AlternativaRepository;
import com.example.foundation.modules.quiz.repository.QuestaoRepository;
import com.example.foundation.modules.quiz.repository.QuizRepository;
import com.example.foundation.modules.quiz.repository.RespostaQuestaoRepository;
import com.example.foundation.modules.quiz.repository.TentativaQuizRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.OperacaoInvalidaException;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuizExecucaoService {

    private final QuizRepository quizRepository;
    private final QuestaoRepository questaoRepository;
    private final AlternativaRepository alternativaRepository;
    private final TentativaQuizRepository tentativaQuizRepository;
    private final RespostaQuestaoRepository respostaQuestaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BiscoitoService biscoitoService;
    private final FlorestaStreakService florestaStreakService;

    public QuizExecucaoService(
            QuizRepository quizRepository,
            QuestaoRepository questaoRepository,
            AlternativaRepository alternativaRepository,
            TentativaQuizRepository tentativaQuizRepository,
            RespostaQuestaoRepository respostaQuestaoRepository,
            UsuarioRepository usuarioRepository,
            BiscoitoService biscoitoService,
            FlorestaStreakService florestaStreakService
    ) {
        this.quizRepository = quizRepository;
        this.questaoRepository = questaoRepository;
        this.alternativaRepository = alternativaRepository;
        this.tentativaQuizRepository = tentativaQuizRepository;
        this.respostaQuestaoRepository = respostaQuestaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.biscoitoService = biscoitoService;
        this.florestaStreakService = florestaStreakService;
    }

    @Transactional(readOnly = true)
    public QuizJogarResponse montarQuizParaJogar(UUID quizId) {
        Quiz quiz = buscarQuiz(quizId);
        List<QuestaoJogarResponse> questoes = questaoRepository
                .findByQuiz_IdAndAtivoTrueOrderByOrdemAsc(quizId)
                .stream()
                .map(this::toQuestaoJogar)
                .toList();

        if (questoes.isEmpty()) {
            throw new OperacaoInvalidaException("Quiz sem questões disponíveis");
        }

        return new QuizJogarResponse(quiz.getId(), quiz.getTitulo(), quiz.getDescricao(), questoes);
    }

    public IniciarTentativaQuizResponse iniciarTentativa(UUID quizId, UUID usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        Quiz quiz = buscarQuiz(quizId);

        tentativaQuizRepository
                .findByUsuario_IdAndQuiz_IdAndRealizadoEmIsNullAndAtivoTrue(usuarioId, quizId)
                .ifPresent(tentativa -> {
                    throw new OperacaoInvalidaException("Já existe uma tentativa em andamento para este quiz");
                });

        TentativaQuiz tentativa = new TentativaQuiz();
        tentativa.setUsuario(usuario);
        tentativa.setQuiz(quiz);
        tentativa = tentativaQuizRepository.save(tentativa);

        QuizJogarResponse quizJogar = montarQuizParaJogar(quizId);
        return new IniciarTentativaQuizResponse(tentativa.getId(), quizId, quizJogar);
    }

    public FinalizarTentativaQuizResponse finalizarTentativa(
            UUID tentativaId,
            UUID usuarioId,
            FinalizarTentativaQuizRequest request
    ) {
        TentativaQuiz tentativa = tentativaQuizRepository.findByIdAndAtivoTrue(tentativaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("TentativaQuiz", tentativaId));

        if (tentativa.getUsuario() == null || !tentativa.getUsuario().getId().equals(usuarioId)) {
            throw new OperacaoInvalidaException("Tentativa não pertence ao usuário autenticado");
        }

        if (tentativa.getRealizadoEm() != null) {
            throw new OperacaoInvalidaException("Tentativa já finalizada");
        }

        Quiz quiz = tentativa.getQuiz();
        if (quiz == null) {
            throw new OperacaoInvalidaException("Tentativa sem quiz associado");
        }

        List<Questao> questoes = questaoRepository.findByQuiz_IdAndAtivoTrueOrderByOrdemAsc(quiz.getId());
        if (questoes.isEmpty()) {
            throw new OperacaoInvalidaException("Quiz sem questões");
        }

        validarRespostasCompletas(questoes, request.respostas());

        int acertos = 0;
        for (RespostaQuizItemRequest item : request.respostas()) {
            Questao questao = questoes.stream()
                    .filter(q -> q.getId().equals(item.questaoId()))
                    .findFirst()
                    .orElseThrow(() -> new OperacaoInvalidaException("Questão inválida para este quiz"));

            Alternativa alternativa = alternativaRepository.findByIdAndAtivoTrue(item.alternativaId())
                    .orElseThrow(() -> new OperacaoInvalidaException("Alternativa inválida"));

            if (alternativa.getQuestao() == null
                    || !alternativa.getQuestao().getId().equals(questao.getId())) {
                throw new OperacaoInvalidaException("Alternativa não pertence à questão informada");
            }

            boolean correta = Boolean.TRUE.equals(alternativa.getCorreta());
            if (correta) {
                acertos++;
            }

            RespostaQuestao resposta = new RespostaQuestao();
            resposta.setTentativaQuiz(tentativa);
            resposta.setQuestao(questao);
            resposta.setAlternativa(alternativa);
            resposta.setCorreta(correta);
            respostaQuestaoRepository.save(resposta);
        }

        int total = questoes.size();
        int pontuacao = Math.round((acertos * 100f) / total);

        tentativa.setAcertos(acertos);
        tentativa.setTotalQuestoes(total);
        tentativa.setPontuacao(pontuacao);
        tentativa.setRealizadoEm(Instant.now());
        tentativaQuizRepository.save(tentativa);

        Usuario usuario = tentativa.getUsuario();
        String motivo = "Quiz concluído: " + quiz.getTitulo();
        biscoitoService.concederBiscoitoQuiz(usuario, motivo);
        florestaStreakService.registrarEstudoConcluido(usuario);
        usuarioRepository.save(usuario);

        Viveiro viveiro = biscoitoService.obterOuCriarViveiro(usuario);

        return new FinalizarTentativaQuizResponse(
                tentativa.getId(),
                acertos,
                total,
                pontuacao,
                true,
                biscoitoService.saldo(viveiro),
                usuario.getStreakAtual(),
                florestaStreakService.calcularSaudeFloresta(usuario)
        );
    }

    private void validarRespostasCompletas(List<Questao> questoes, List<RespostaQuizItemRequest> respostas) {
        Set<UUID> questoesEsperadas = new HashSet<>();
        questoes.forEach(q -> questoesEsperadas.add(q.getId()));

        Set<UUID> questoesRespondidas = new HashSet<>();

        for (RespostaQuizItemRequest resposta : respostas) {
            if (!questoesEsperadas.contains(resposta.questaoId())) {
                throw new OperacaoInvalidaException("Resposta para questão fora do quiz");
            }
            if (!questoesRespondidas.add(resposta.questaoId())) {
                throw new OperacaoInvalidaException("Questão respondida mais de uma vez");
            }
        }

        if (questoesRespondidas.size() != questoesEsperadas.size()) {
            throw new OperacaoInvalidaException("Responda todas as questões antes de finalizar");
        }
    }

    private QuestaoJogarResponse toQuestaoJogar(Questao questao) {
        List<AlternativaJogarResponse> alternativas = alternativaRepository
                .findByQuestao_IdAndAtivoTrueOrderByOrdemAsc(questao.getId())
                .stream()
                .map(alt -> new AlternativaJogarResponse(alt.getId(), alt.getTexto(), alt.getOrdem()))
                .toList();

        return new QuestaoJogarResponse(
                questao.getId(),
                questao.getEnunciado(),
                questao.getOrdem(),
                alternativas
        );
    }

    private Quiz buscarQuiz(UUID quizId) {
        return quizRepository.findByIdAndAtivoTrue(quizId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quiz", quizId));
    }

    private Usuario buscarUsuario(UUID usuarioId) {
        return usuarioRepository.findByIdAndAtivoTrue(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", usuarioId));
    }
}
