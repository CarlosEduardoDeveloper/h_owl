package com.example.foundation.modules.quiz.service;

import com.example.foundation.modules.quiz.domain.Questao;
import com.example.foundation.modules.quiz.domain.Quiz;
import com.example.foundation.modules.quiz.dto.QuestaoRequest;
import com.example.foundation.modules.quiz.dto.QuestaoResponse;
import com.example.foundation.modules.quiz.mapper.QuestaoMapper;
import com.example.foundation.modules.quiz.repository.QuestaoRepository;
import com.example.foundation.modules.quiz.repository.QuizRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuestaoService {

    private final QuestaoRepository repository;
    private final QuizRepository quizRepository;

    public QuestaoService(
            QuestaoRepository repository,
            QuizRepository quizRepository
    ) {
        this.repository = repository;
        this.quizRepository = quizRepository;
    }

    @Transactional(readOnly = true)
    public List<QuestaoResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(QuestaoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestaoResponse buscarAtivo(UUID id) {
        return QuestaoMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public QuestaoResponse criar(QuestaoRequest request) {
        Questao entity = QuestaoMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return QuestaoMapper.toResponse(repository.save(entity));
    }

    public QuestaoResponse atualizar(UUID id, QuestaoRequest request) {
        Questao entity = buscarEntidadeAtiva(id);
        QuestaoMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return QuestaoMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Questao entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Questao buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Questao", id));
    }

    private void aplicarRelacionamentos(Questao entity, QuestaoRequest request) {


        if (request.quizId() != null) {
            Quiz quiz = quizRepository.findByIdAndAtivoTrue(request.quizId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Quiz", request.quizId()));
            entity.setQuiz(quiz);
        } else {
            entity.setQuiz(null);
        }
    }
}