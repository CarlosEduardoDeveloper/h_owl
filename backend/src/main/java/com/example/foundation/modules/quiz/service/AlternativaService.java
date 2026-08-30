package com.example.foundation.modules.quiz.service;

import com.example.foundation.modules.quiz.domain.Alternativa;
import com.example.foundation.modules.quiz.domain.Questao;
import com.example.foundation.modules.quiz.dto.AlternativaRequest;
import com.example.foundation.modules.quiz.dto.AlternativaResponse;
import com.example.foundation.modules.quiz.mapper.AlternativaMapper;
import com.example.foundation.modules.quiz.repository.AlternativaRepository;
import com.example.foundation.modules.quiz.repository.QuestaoRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlternativaService {

    private final AlternativaRepository repository;
    private final QuestaoRepository questaoRepository;

    public AlternativaService(
            AlternativaRepository repository,
            QuestaoRepository questaoRepository
    ) {
        this.repository = repository;
        this.questaoRepository = questaoRepository;
    }

    @Transactional(readOnly = true)
    public List<AlternativaResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(AlternativaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlternativaResponse buscarAtivo(UUID id) {
        return AlternativaMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public AlternativaResponse criar(AlternativaRequest request) {
        Alternativa entity = AlternativaMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return AlternativaMapper.toResponse(repository.save(entity));
    }

    public AlternativaResponse atualizar(UUID id, AlternativaRequest request) {
        Alternativa entity = buscarEntidadeAtiva(id);
        AlternativaMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return AlternativaMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Alternativa entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Alternativa buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Alternativa", id));
    }

    private void aplicarRelacionamentos(Alternativa entity, AlternativaRequest request) {


        if (request.questaoId() != null) {
            Questao questao = questaoRepository.findByIdAndAtivoTrue(request.questaoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Questao", request.questaoId()));
            entity.setQuestao(questao);
        } else {
            entity.setQuestao(null);
        }
    }
}