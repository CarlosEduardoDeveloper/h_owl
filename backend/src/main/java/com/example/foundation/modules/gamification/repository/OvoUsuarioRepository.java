package com.example.foundation.modules.gamification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.OvoUsuario;
import com.example.foundation.modules.gamification.domain.enums.OvoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OvoUsuarioRepository extends JpaRepository<OvoUsuario, UUID> {

    List<OvoUsuario> findByAtivoTrue();

    Optional<OvoUsuario> findByIdAndAtivoTrue(UUID id);

    Optional<OvoUsuario> findFirstByUsuario_IdAndStatusAndAtivoTrueOrderByCriadoEmDesc(
            UUID usuarioId,
            OvoStatus status
    );

    Optional<OvoUsuario> findBySessaoEstudo_IdAndAtivoTrue(UUID sessaoEstudoId);
}