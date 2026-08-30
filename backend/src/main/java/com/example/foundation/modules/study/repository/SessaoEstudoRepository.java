package com.example.foundation.modules.study.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.study.domain.SessaoEstudo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoEstudoRepository extends JpaRepository<SessaoEstudo, UUID> {

    List<SessaoEstudo> findByAtivoTrue();

    Optional<SessaoEstudo> findByIdAndAtivoTrue(UUID id);
}