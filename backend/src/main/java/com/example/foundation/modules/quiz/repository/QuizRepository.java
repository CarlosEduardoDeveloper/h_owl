package com.example.foundation.modules.quiz.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {

    List<Quiz> findByAtivoTrue();

    Optional<Quiz> findByIdAndAtivoTrue(UUID id);
}