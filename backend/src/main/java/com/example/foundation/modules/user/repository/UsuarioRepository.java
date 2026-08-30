package com.example.foundation.modules.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.user.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    List<Usuario> findByAtivoTrue();

    Optional<Usuario> findByIdAndAtivoTrue(UUID id);
}