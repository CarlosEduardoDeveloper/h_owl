package com.example.foundation.modules.learning.mapper;

import com.example.foundation.modules.learning.domain.ProgressoTrilha;
import com.example.foundation.modules.learning.dto.ProgressoTrilhaRequest;
import com.example.foundation.modules.learning.dto.ProgressoTrilhaResponse;
import com.example.foundation.modules.user.domain.Usuario;

public final class ProgressoTrilhaMapper {

    private ProgressoTrilhaMapper() {
    }

    public static ProgressoTrilhaResponse toResponse(ProgressoTrilha entity) {
        return new ProgressoTrilhaResponse(
                entity.getId(),
                entity.getStatus(),
                entity.getProgressoPercentual(),
                entity.getUltimoAcessoEm(),
                entity.getConcluidoEm(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getTrilha() != null ? entity.getTrilha().getId() : null,
                entity.getModuloAtual() != null ? entity.getModuloAtual().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static ProgressoTrilha toEntity(ProgressoTrilhaRequest request) {
        ProgressoTrilha entity = new ProgressoTrilha();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(ProgressoTrilha entity, ProgressoTrilhaRequest request) {
        entity.setStatus(request.status());
        entity.setProgressoPercentual(request.progressoPercentual());
        entity.setUltimoAcessoEm(request.ultimoAcessoEm());
        entity.setConcluidoEm(request.concluidoEm());
    }
}