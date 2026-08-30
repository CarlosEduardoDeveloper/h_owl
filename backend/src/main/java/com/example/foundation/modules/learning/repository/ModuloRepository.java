package com.example.foundation.modules.learning.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.learning.domain.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuloRepository extends JpaRepository<Modulo, UUID> {

    List<Modulo> findByAtivoTrue();

    Optional<Modulo> findByIdAndAtivoTrue(UUID id);
}