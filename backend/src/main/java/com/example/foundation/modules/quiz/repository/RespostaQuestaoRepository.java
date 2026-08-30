package com.example.foundation.modules.quiz.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.quiz.domain.RespostaQuestao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespostaQuestaoRepository extends JpaRepository<RespostaQuestao, UUID> {

    List<RespostaQuestao> findByAtivoTrue();

    Optional<RespostaQuestao> findByIdAndAtivoTrue(UUID id);
}