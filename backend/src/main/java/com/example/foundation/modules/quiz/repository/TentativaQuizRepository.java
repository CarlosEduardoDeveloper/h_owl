package com.example.foundation.modules.quiz.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.quiz.domain.TentativaQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TentativaQuizRepository extends JpaRepository<TentativaQuiz, UUID> {

    List<TentativaQuiz> findByAtivoTrue();

    Optional<TentativaQuiz> findByIdAndAtivoTrue(UUID id);

    Optional<TentativaQuiz> findByUsuario_IdAndQuiz_IdAndRealizadoEmIsNullAndAtivoTrue(
            UUID usuarioId,
            UUID quizId
    );
}