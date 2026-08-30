package com.example.foundation.modules.learning.mapper;

import com.example.foundation.modules.learning.domain.Modulo;
import com.example.foundation.modules.learning.dto.ModuloRequest;
import com.example.foundation.modules.learning.dto.ModuloResponse;

public final class ModuloMapper {

    private ModuloMapper() {
    }

    public static ModuloResponse toResponse(Modulo entity) {
        return new ModuloResponse(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getOrdem(),
                entity.getTempoSugeridoMinutos(),
                entity.getTrilha() != null ? entity.getTrilha().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Modulo toEntity(ModuloRequest request) {
        Modulo entity = new Modulo();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Modulo entity, ModuloRequest request) {
        entity.setTitulo(request.titulo());
        entity.setDescricao(request.descricao());
        entity.setOrdem(request.ordem());
        entity.setTempoSugeridoMinutos(request.tempoSugeridoMinutos());
    }
}