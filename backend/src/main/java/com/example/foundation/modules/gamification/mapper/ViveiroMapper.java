package com.example.foundation.modules.gamification.mapper;

import com.example.foundation.modules.gamification.domain.Viveiro;
import com.example.foundation.modules.gamification.dto.ViveiroRequest;
import com.example.foundation.modules.gamification.dto.ViveiroResponse;
import com.example.foundation.modules.user.domain.Usuario;

public final class ViveiroMapper {

    private ViveiroMapper() {
    }

    public static ViveiroResponse toResponse(Viveiro entity) {
        return new ViveiroResponse(
                entity.getId(),
                entity.getNome(),
                entity.getNivel(),
                entity.getXpTotal(),
                entity.getTemaVisual(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Viveiro toEntity(ViveiroRequest request) {
        Viveiro entity = new Viveiro();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Viveiro entity, ViveiroRequest request) {
        entity.setNome(request.nome());
        entity.setNivel(request.nivel());
        entity.setXpTotal(request.xpTotal());
        entity.setTemaVisual(request.temaVisual());
    }
}