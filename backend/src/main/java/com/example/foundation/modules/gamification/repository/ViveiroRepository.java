package com.example.foundation.modules.gamification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.Viveiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViveiroRepository extends JpaRepository<Viveiro, UUID> {

    List<Viveiro> findByAtivoTrue();

    Optional<Viveiro> findByIdAndAtivoTrue(UUID id);

    Optional<Viveiro> findFirstByUsuario_IdAndAtivoTrueOrderByCriadoEmDesc(UUID usuarioId);
}