package com.example.foundation.modules.gamification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.Coruja;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorujaRepository extends JpaRepository<Coruja, UUID> {

    List<Coruja> findByAtivoTrue();

    Optional<Coruja> findByIdAndAtivoTrue(UUID id);
}