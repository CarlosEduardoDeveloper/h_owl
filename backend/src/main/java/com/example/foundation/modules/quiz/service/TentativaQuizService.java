package com.example.foundation.modules.quiz.service;

import com.example.foundation.modules.quiz.domain.Quiz;
import com.example.foundation.modules.quiz.domain.TentativaQuiz;
import com.example.foundation.modules.quiz.dto.TentativaQuizRequest;
import com.example.foundation.modules.quiz.dto.TentativaQuizResponse;
import com.example.foundation.modules.quiz.mapper.TentativaQuizMapper;
import com.example.foundation.modules.quiz.repository.QuizRepository;
import com.example.foundation.modules.quiz.repository.TentativaQuizRepository;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.repository.SessaoEstudoRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TentativaQuizService {

    private final TentativaQuizRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final QuizRepository quizRepository;
    private final SessaoEstudoRepository sessaoEstudoRepository;

    public TentativaQuizService(
            TentativaQuizRepository repository,
            UsuarioRepository usuarioRepository,
            QuizRepository quizRepository,
            SessaoEstudoRepository sessaoEstudoRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.quizRepository = quizRepository;
        this.sessaoEstudoRepository = sessaoEstudoRepository;
    }

    @Transactional(readOnly = true)
    public List<TentativaQuizResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(TentativaQuizMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TentativaQuizResponse buscarAtivo(UUID id) {
        return TentativaQuizMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public TentativaQuizResponse criar(TentativaQuizRequest request) {
        TentativaQuiz entity = TentativaQuizMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return TentativaQuizMapper.toResponse(repository.save(entity));
    }

    public TentativaQuizResponse atualizar(UUID id, TentativaQuizRequest request) {
        TentativaQuiz entity = buscarEntidadeAtiva(id);
        TentativaQuizMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return TentativaQuizMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        TentativaQuiz entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private TentativaQuiz buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("TentativaQuiz", id));
    }

    private void aplicarRelacionamentos(TentativaQuiz entity, TentativaQuizRequest request) {


        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(request.usuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
            entity.setUsuario(usuario);
        } else {
            entity.setUsuario(null);
        }

        if (request.quizId() != null) {
            Quiz quiz = quizRepository.findByIdAndAtivoTrue(request.quizId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Quiz", request.quizId()));
            entity.setQuiz(quiz);
        } else {
            entity.setQuiz(null);
        }

        if (request.sessaoEstudoId() != null) {
            SessaoEstudo sessaoEstudo = sessaoEstudoRepository.findByIdAndAtivoTrue(request.sessaoEstudoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("SessaoEstudo", request.sessaoEstudoId()));
            entity.setSessaoEstudo(sessaoEstudo);
        } else {
            entity.setSessaoEstudo(null);
        }
    }
}