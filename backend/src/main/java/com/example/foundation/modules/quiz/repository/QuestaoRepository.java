package com.example.foundation.modules.quiz.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.quiz.domain.Questao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestaoRepository extends JpaRepository<Questao, UUID> {

    List<Questao> findByAtivoTrue();

    Optional<Questao> findByIdAndAtivoTrue(UUID id);

    List<Questao> findByQuiz_IdAndAtivoTrueOrderByOrdemAsc(UUID quizId);
}