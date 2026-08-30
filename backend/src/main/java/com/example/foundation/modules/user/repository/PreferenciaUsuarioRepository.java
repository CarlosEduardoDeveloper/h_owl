package com.example.foundation.modules.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.user.domain.PreferenciaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenciaUsuarioRepository extends JpaRepository<PreferenciaUsuario, UUID> {

    List<PreferenciaUsuario> findByAtivoTrue();

    Optional<PreferenciaUsuario> findByIdAndAtivoTrue(UUID id);
}