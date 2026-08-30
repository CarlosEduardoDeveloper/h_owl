package com.example.foundation.modules.gamification.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.TipoOvo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoOvoRepository extends JpaRepository<TipoOvo, UUID> {

    List<TipoOvo> findByAtivoTrue();

    Optional<TipoOvo> findByIdAndAtivoTrue(UUID id);

    Optional<TipoOvo> findFirstByDuracaoMinimaMinutosAndAtivoTrue(Integer duracaoMinimaMinutos);
}