package com.example.foundation.modules.gamification.mapper;

import com.example.foundation.modules.gamification.domain.Conquista;
import com.example.foundation.modules.gamification.dto.ConquistaRequest;
import com.example.foundation.modules.gamification.dto.ConquistaResponse;

public final class ConquistaMapper {

    private ConquistaMapper() {
    }

    public static ConquistaResponse toResponse(Conquista entity) {
        return new ConquistaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getIconeUrl(),
                entity.getCategoria(),
                entity.getRegra(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Conquista toEntity(ConquistaRequest request) {
        Conquista entity = new Conquista();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Conquista entity, ConquistaRequest request) {
        entity.setNome(request.nome());
        entity.setDescricao(request.descricao());
        entity.setIconeUrl(request.iconeUrl());
        entity.setCategoria(request.categoria());
        entity.setRegra(request.regra());
    }
}