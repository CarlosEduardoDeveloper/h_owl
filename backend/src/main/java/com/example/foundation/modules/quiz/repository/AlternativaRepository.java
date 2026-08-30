package com.example.foundation.modules.quiz.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.quiz.domain.Alternativa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlternativaRepository extends JpaRepository<Alternativa, UUID> {

    List<Alternativa> findByAtivoTrue();

    Optional<Alternativa> findByIdAndAtivoTrue(UUID id);
}