package com.example.foundation.modules.study.mapper;

import com.example.foundation.modules.study.domain.ConteudoSessao;
import com.example.foundation.modules.study.dto.ConteudoSessaoRequest;
import com.example.foundation.modules.study.dto.ConteudoSessaoResponse;

public final class ConteudoSessaoMapper {

    private ConteudoSessaoMapper() {
    }

    public static ConteudoSessaoResponse toResponse(ConteudoSessao entity) {
        return new ConteudoSessaoResponse(
                entity.getId(),
                entity.getTipo(),
                entity.getReferenciaId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getOrdem(),
                entity.getSessaoEstudo() != null ? entity.getSessaoEstudo().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static ConteudoSessao toEntity(ConteudoSessaoRequest request) {
        ConteudoSessao entity = new ConteudoSessao();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(ConteudoSessao entity, ConteudoSessaoRequest request) {
        entity.setTipo(request.tipo());
        entity.setReferenciaId(request.referenciaId());
        entity.setTitulo(request.titulo());
        entity.setDescricao(request.descricao());
        entity.setOrdem(request.ordem());
    }
}