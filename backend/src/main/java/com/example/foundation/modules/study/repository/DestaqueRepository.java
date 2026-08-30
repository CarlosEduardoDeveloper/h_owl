package com.example.foundation.modules.study.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.study.domain.Destaque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestaqueRepository extends JpaRepository<Destaque, UUID> {

    List<Destaque> findByAtivoTrue();

    Optional<Destaque> findByIdAndAtivoTrue(UUID id);
}