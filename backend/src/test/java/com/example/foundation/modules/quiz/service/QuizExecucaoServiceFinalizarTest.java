package com.example.foundation.modules.quiz.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.service.BiscoitoService;
import com.example.foundation.modules.gamification.service.FlorestaStreakService;
import com.example.foundation.modules.quiz.domain.Alternativa;
import com.example.foundation.modules.quiz.domain.Questao;
import com.example.foundation.modules.quiz.domain.Quiz;
import com.example.foundation.modules.quiz.domain.TentativaQuiz;
import com.example.foundation.modules.quiz.dto.FinalizarTentativaQuizRequest;
import com.example.foundation.modules.quiz.dto.RespostaQuizItemRequest;
import com.example.foundation.modules.quiz.repository.AlternativaRepository;
import com.example.foundation.modules.quiz.repository.QuestaoRepository;
import com.example.foundation.modules.quiz.repository.QuizRepository;
import com.example.foundation.modules.quiz.repository.RespostaQuestaoRepository;
import com.example.foundation.modules.quiz.repository.TentativaQuizRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.OperacaoInvalidaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizExecucaoServiceFinalizarTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestaoRepository questaoRepository;

    @Mock
    private AlternativaRepository alternativaRepository;

    @Mock
    private TentativaQuizRepository tentativaQuizRepository;

    @Mock
    private RespostaQuestaoRepository respostaQuestaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BiscoitoService biscoitoService;

    @Mock
    private FlorestaStreakService florestaStreakService;

    @InjectMocks
    private QuizExecucaoService service;

    @Test
    void finalizarConcedeBiscoito() {
        UUID usuarioId = UUID.randomUUID();
        UUID tentativaId = UUID.randomUUID();
        UUID quizId = UUID.randomUUID();
        UUID questaoId = UUID.randomUUID();
        UUID alternativaId = UUID.randomUUID();

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setTitulo("Demo");

        TentativaQuiz tentativa = new TentativaQuiz();
        tentativa.setId(tentativaId);
        tentativa.setUsuario(usuario);
        tentativa.setQuiz(quiz);

        Questao questao = new Questao();
        questao.setId(questaoId);
        questao.setQuiz(quiz);

        Alternativa alternativa = new Alternativa();
        alternativa.setId(alternativaId);
        alternativa.setQuestao(questao);
        alternativa.setCorreta(true);

        when(tentativaQuizRepository.findByIdAndAtivoTrue(tentativaId)).thenReturn(Optional.of(tentativa));
        when(questaoRepository.findByQuiz_IdAndAtivoTrueOrderByOrdemAsc(quizId)).thenReturn(List.of(questao));
        when(alternativaRepository.findByIdAndAtivoTrue(alternativaId)).thenReturn(Optional.of(alternativa));
        when(respostaQuestaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(tentativaQuizRepository.save(any(TentativaQuiz.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(biscoitoService.obterOuCriarViveiro(usuario)).thenReturn(new com.example.foundation.modules.gamification.domain.Viveiro());
        when(biscoitoService.saldo(any())).thenReturn(1);

        var response = service.finalizarTentativa(
                tentativaId,
                usuarioId,
                new FinalizarTentativaQuizRequest(List.of(new RespostaQuizItemRequest(questaoId, alternativaId)))
        );

        assertThat(response.biscoitoConcedido()).isTrue();
        assertThat(response.acertos()).isEqualTo(1);
        verify(biscoitoService).concederBiscoitoQuiz(eq(usuario), eq("Quiz concluído: Demo"));
        verify(florestaStreakService).registrarEstudoConcluido(usuario);
    }
}
