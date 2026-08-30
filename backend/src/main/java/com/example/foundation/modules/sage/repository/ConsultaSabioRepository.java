package com.example.foundation.modules.sage.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.sage.domain.ConsultaSabio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaSabioRepository extends JpaRepository<ConsultaSabio, UUID> {

    List<ConsultaSabio> findByAtivoTrue();

    Optional<ConsultaSabio> findByIdAndAtivoTrue(UUID id);
}