package com.example.foundation.modules.gamification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.Recompensa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecompensaRepository extends JpaRepository<Recompensa, UUID> {

    List<Recompensa> findByAtivoTrue();

    Optional<Recompensa> findByIdAndAtivoTrue(UUID id);
}