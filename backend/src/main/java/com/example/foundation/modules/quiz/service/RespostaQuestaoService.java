package com.example.foundation.modules.quiz.service;

import com.example.foundation.modules.quiz.domain.Alternativa;
import com.example.foundation.modules.quiz.domain.Questao;
import com.example.foundation.modules.quiz.domain.RespostaQuestao;
import com.example.foundation.modules.quiz.domain.TentativaQuiz;
import com.example.foundation.modules.quiz.dto.RespostaQuestaoRequest;
import com.example.foundation.modules.quiz.dto.RespostaQuestaoResponse;
import com.example.foundation.modules.quiz.mapper.RespostaQuestaoMapper;
import com.example.foundation.modules.quiz.repository.AlternativaRepository;
import com.example.foundation.modules.quiz.repository.QuestaoRepository;
import com.example.foundation.modules.quiz.repository.RespostaQuestaoRepository;
import com.example.foundation.modules.quiz.repository.TentativaQuizRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RespostaQuestaoService {

    private final RespostaQuestaoRepository repository;
    private final TentativaQuizRepository tentativaQuizRepository;
    private final QuestaoRepository questaoRepository;
    private final AlternativaRepository alternativaRepository;

    public RespostaQuestaoService(
            RespostaQuestaoRepository repository,
            TentativaQuizRepository tentativaQuizRepository,
            QuestaoRepository questaoRepository,
            AlternativaRepository alternativaRepository
    ) {
        this.repository = repository;
        this.tentativaQuizRepository = tentativaQuizRepository;
        this.questaoRepository = questaoRepository;
        this.alternativaRepository = alternativaRepository;
    }

    @Transactional(readOnly = true)
    public List<RespostaQuestaoResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(RespostaQuestaoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RespostaQuestaoResponse buscarAtivo(UUID id) {
        return RespostaQuestaoMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public RespostaQuestaoResponse criar(RespostaQuestaoRequest request) {
        RespostaQuestao entity = RespostaQuestaoMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return RespostaQuestaoMapper.toResponse(repository.save(entity));
    }

    public RespostaQuestaoResponse atualizar(UUID id, RespostaQuestaoRequest request) {
        RespostaQuestao entity = buscarEntidadeAtiva(id);
        RespostaQuestaoMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return RespostaQuestaoMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        RespostaQuestao entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private RespostaQuestao buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("RespostaQuestao", id));
    }

    private void aplicarRelacionamentos(RespostaQuestao entity, RespostaQuestaoRequest request) {


        if (request.tentativaQuizId() != null) {
            TentativaQuiz tentativaQuiz = tentativaQuizRepository.findByIdAndAtivoTrue(request.tentativaQuizId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("TentativaQuiz", request.tentativaQuizId()));
            entity.setTentativaQuiz(tentativaQuiz);
        } else {
            entity.setTentativaQuiz(null);
        }

        if (request.questaoId() != null) {
            Questao questao = questaoRepository.findByIdAndAtivoTrue(request.questaoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Questao", request.questaoId()));
            entity.setQuestao(questao);
        } else {
            entity.setQuestao(null);
        }

        if (request.alternativaId() != null) {
            Alternativa alternativa = alternativaRepository.findByIdAndAtivoTrue(request.alternativaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Alternativa", request.alternativaId()));
            entity.setAlternativa(alternativa);
        } else {
            entity.setAlternativa(null);
        }
    }
}