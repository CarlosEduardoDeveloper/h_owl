package com.example.foundation.modules.gamification.mapper;

import com.example.foundation.modules.gamification.domain.Coruja;
import com.example.foundation.modules.gamification.dto.CorujaRequest;
import com.example.foundation.modules.gamification.dto.CorujaResponse;

public final class CorujaMapper {

    private CorujaMapper() {
    }

    public static CorujaResponse toResponse(Coruja entity) {
        return new CorujaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getEspecie(),
                entity.getRaridade(),
                entity.getDescricao(),
                entity.getImagemUrl(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Coruja toEntity(CorujaRequest request) {
        Coruja entity = new Coruja();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Coruja entity, CorujaRequest request) {
        entity.setNome(request.nome());
        entity.setEspecie(request.especie());
        entity.setRaridade(request.raridade());
        entity.setDescricao(request.descricao());
        entity.setImagemUrl(request.imagemUrl());
    }
}