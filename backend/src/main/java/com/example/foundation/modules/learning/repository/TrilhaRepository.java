package com.example.foundation.modules.learning.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.learning.domain.Trilha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrilhaRepository extends JpaRepository<Trilha, UUID> {

    List<Trilha> findByAtivoTrue();

    Optional<Trilha> findByIdAndAtivoTrue(UUID id);
}