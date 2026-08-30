package com.example.foundation.modules.learning.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.learning.domain.ProgressoTrilha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressoTrilhaRepository extends JpaRepository<ProgressoTrilha, UUID> {

    List<ProgressoTrilha> findByAtivoTrue();

    Optional<ProgressoTrilha> findByIdAndAtivoTrue(UUID id);

    List<ProgressoTrilha> findByUsuario_IdAndAtivoTrueOrderByUltimoAcessoEmDesc(UUID usuarioId);
}