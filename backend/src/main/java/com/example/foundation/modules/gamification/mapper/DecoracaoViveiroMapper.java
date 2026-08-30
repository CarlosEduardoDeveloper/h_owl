package com.example.foundation.modules.gamification.mapper;

import com.example.foundation.modules.gamification.domain.DecoracaoViveiro;
import com.example.foundation.modules.gamification.dto.DecoracaoViveiroRequest;
import com.example.foundation.modules.gamification.dto.DecoracaoViveiroResponse;

public final class DecoracaoViveiroMapper {

    private DecoracaoViveiroMapper() {
    }

    public static DecoracaoViveiroResponse toResponse(DecoracaoViveiro entity) {
        return new DecoracaoViveiroResponse(
                entity.getId(),
                entity.getNome(),
                entity.getTipo(),
                entity.getImagemUrl(),
                entity.getAdquiridaEm(),
                entity.getViveiro() != null ? entity.getViveiro().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static DecoracaoViveiro toEntity(DecoracaoViveiroRequest request) {
        DecoracaoViveiro entity = new DecoracaoViveiro();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(DecoracaoViveiro entity, DecoracaoViveiroRequest request) {
        entity.setNome(request.nome());
        entity.setTipo(request.tipo());
        entity.setImagemUrl(request.imagemUrl());
        entity.setAdquiridaEm(request.adquiridaEm());
    }
}