package com.example.foundation.modules.gamification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.OvoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OvoUsuarioRepository extends JpaRepository<OvoUsuario, UUID> {

    List<OvoUsuario> findByAtivoTrue();

    Optional<OvoUsuario> findByIdAndAtivoTrue(UUID id);
}