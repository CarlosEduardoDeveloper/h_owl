package com.example.foundation.modules.gamification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.DecoracaoViveiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DecoracaoViveiroRepository extends JpaRepository<DecoracaoViveiro, UUID> {

    List<DecoracaoViveiro> findByAtivoTrue();

    Optional<DecoracaoViveiro> findByIdAndAtivoTrue(UUID id);
}