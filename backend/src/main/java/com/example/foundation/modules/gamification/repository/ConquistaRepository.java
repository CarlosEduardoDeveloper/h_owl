package com.example.foundation.modules.gamification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.Conquista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConquistaRepository extends JpaRepository<Conquista, UUID> {

    List<Conquista> findByAtivoTrue();

    Optional<Conquista> findByIdAndAtivoTrue(UUID id);
}