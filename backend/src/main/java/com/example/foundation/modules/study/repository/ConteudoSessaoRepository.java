package com.example.foundation.modules.study.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.study.domain.ConteudoSessao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConteudoSessaoRepository extends JpaRepository<ConteudoSessao, UUID> {

    List<ConteudoSessao> findByAtivoTrue();

    Optional<ConteudoSessao> findByIdAndAtivoTrue(UUID id);
}