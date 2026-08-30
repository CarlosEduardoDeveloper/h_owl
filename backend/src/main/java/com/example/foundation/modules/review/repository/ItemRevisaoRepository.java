package com.example.foundation.modules.review.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.review.domain.ItemRevisao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRevisaoRepository extends JpaRepository<ItemRevisao, UUID> {

    List<ItemRevisao> findByAtivoTrue();

    Optional<ItemRevisao> findByIdAndAtivoTrue(UUID id);
}