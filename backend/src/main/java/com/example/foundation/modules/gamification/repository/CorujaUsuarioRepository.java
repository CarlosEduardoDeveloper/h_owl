package com.example.foundation.modules.gamification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.CorujaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorujaUsuarioRepository extends JpaRepository<CorujaUsuario, UUID> {

    List<CorujaUsuario> findByAtivoTrue();

    Optional<CorujaUsuario> findByIdAndAtivoTrue(UUID id);
}