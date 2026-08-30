package com.example.foundation.modules.quiz.service;

import com.example.foundation.modules.learning.domain.Modulo;
import com.example.foundation.modules.learning.repository.ModuloRepository;
import com.example.foundation.modules.quiz.domain.Quiz;
import com.example.foundation.modules.quiz.dto.QuizRequest;
import com.example.foundation.modules.quiz.dto.QuizResponse;
import com.example.foundation.modules.quiz.mapper.QuizMapper;
import com.example.foundation.modules.quiz.repository.QuizRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuizService {

    private final QuizRepository repository;
    private final ModuloRepository moduloRepository;

    public QuizService(
            QuizRepository repository,
            ModuloRepository moduloRepository
    ) {
        this.repository = repository;
        this.moduloRepository = moduloRepository;
    }

    @Transactional(readOnly = true)
    public List<QuizResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(QuizMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuizResponse buscarAtivo(UUID id) {
        return QuizMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public QuizResponse criar(QuizRequest request) {
        Quiz entity = QuizMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return QuizMapper.toResponse(repository.save(entity));
    }

    public QuizResponse atualizar(UUID id, QuizRequest request) {
        Quiz entity = buscarEntidadeAtiva(id);
        QuizMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return QuizMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Quiz entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Quiz buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quiz", id));
    }

    private void aplicarRelacionamentos(Quiz entity, QuizRequest request) {


        if (request.moduloId() != null) {
            Modulo modulo = moduloRepository.findByIdAndAtivoTrue(request.moduloId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Modulo", request.moduloId()));
            entity.setModulo(modulo);
        } else {
            entity.setModulo(null);
        }
    }
}