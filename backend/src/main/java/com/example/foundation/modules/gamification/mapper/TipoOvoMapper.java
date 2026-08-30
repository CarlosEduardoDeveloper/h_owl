package com.example.foundation.modules.gamification.mapper;

import com.example.foundation.modules.gamification.domain.TipoOvo;
import com.example.foundation.modules.gamification.dto.TipoOvoRequest;
import com.example.foundation.modules.gamification.dto.TipoOvoResponse;

public final class TipoOvoMapper {

    private TipoOvoMapper() {
    }

    public static TipoOvoResponse toResponse(TipoOvo entity) {
        return new TipoOvoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getRaridade(),
                entity.getDuracaoMinimaMinutos(),
                entity.getDuracaoMaximaMinutos(),
                entity.getImagemUrl(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static TipoOvo toEntity(TipoOvoRequest request) {
        TipoOvo entity = new TipoOvo();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(TipoOvo entity, TipoOvoRequest request) {
        entity.setNome(request.nome());
        entity.setRaridade(request.raridade());
        entity.setDuracaoMinimaMinutos(request.duracaoMinimaMinutos());
        entity.setDuracaoMaximaMinutos(request.duracaoMaximaMinutos());
        entity.setImagemUrl(request.imagemUrl());
    }
}