package com.example.foundation.modules.gamification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.ConquistaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConquistaUsuarioRepository extends JpaRepository<ConquistaUsuario, UUID> {

    List<ConquistaUsuario> findByAtivoTrue();

    Optional<ConquistaUsuario> findByIdAndAtivoTrue(UUID id);
}