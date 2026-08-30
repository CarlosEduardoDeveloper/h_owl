package com.example.foundation.modules.learning.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.learning.domain.Conteudo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConteudoRepository extends JpaRepository<Conteudo, UUID> {

    List<Conteudo> findByAtivoTrue();

    Optional<Conteudo> findByIdAndAtivoTrue(UUID id);
}