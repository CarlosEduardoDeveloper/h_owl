package com.example.foundation.modules.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.user.domain.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {

    List<Pessoa> findByAtivoTrue();

    Optional<Pessoa> findByIdAndAtivoTrue(UUID id);
}