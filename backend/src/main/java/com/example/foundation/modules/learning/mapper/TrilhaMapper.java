package com.example.foundation.modules.learning.mapper;

import com.example.foundation.modules.learning.domain.Trilha;
import com.example.foundation.modules.learning.dto.TrilhaRequest;
import com.example.foundation.modules.learning.dto.TrilhaResponse;

public final class TrilhaMapper {

    private TrilhaMapper() {
    }

    public static TrilhaResponse toResponse(Trilha entity) {
        return new TrilhaResponse(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getImagemUrl(),
                entity.getNivelDificuldade(),
                entity.getOrdem(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Trilha toEntity(TrilhaRequest request) {
        Trilha entity = new Trilha();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Trilha entity, TrilhaRequest request) {
        entity.setTitulo(request.titulo());
        entity.setDescricao(request.descricao());
        entity.setImagemUrl(request.imagemUrl());
        entity.setNivelDificuldade(request.nivelDificuldade());
        entity.setOrdem(request.ordem());
    }
}