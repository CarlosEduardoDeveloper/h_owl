package com.example.foundation.modules.study.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.study.domain.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, UUID> {

    List<Nota> findByAtivoTrue();

    Optional<Nota> findByIdAndAtivoTrue(UUID id);
}