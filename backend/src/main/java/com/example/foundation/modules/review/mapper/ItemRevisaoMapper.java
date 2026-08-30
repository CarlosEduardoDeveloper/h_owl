package com.example.foundation.modules.review.mapper;

import com.example.foundation.modules.review.domain.ItemRevisao;
import com.example.foundation.modules.review.dto.ItemRevisaoRequest;
import com.example.foundation.modules.review.dto.ItemRevisaoResponse;
import com.example.foundation.modules.user.domain.Usuario;

public final class ItemRevisaoMapper {

    private ItemRevisaoMapper() {
    }

    public static ItemRevisaoResponse toResponse(ItemRevisao entity) {
        return new ItemRevisaoResponse(
                entity.getId(),
                entity.getTipo(),
                entity.getReferenciaId(),
                entity.getProximaRevisaoEm(),
                entity.getIntervaloDias(),
                entity.getFacilidade(),
                entity.getRepeticoes(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static ItemRevisao toEntity(ItemRevisaoRequest request) {
        ItemRevisao entity = new ItemRevisao();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(ItemRevisao entity, ItemRevisaoRequest request) {
        entity.setTipo(request.tipo());
        entity.setReferenciaId(request.referenciaId());
        entity.setProximaRevisaoEm(request.proximaRevisaoEm());
        entity.setIntervaloDias(request.intervaloDias());
        entity.setFacilidade(request.facilidade());
        entity.setRepeticoes(request.repeticoes());
    }
}