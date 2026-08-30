package com.example.foundation.modules.study.mapper;

import com.example.foundation.modules.study.domain.Destaque;
import com.example.foundation.modules.study.dto.DestaqueRequest;
import com.example.foundation.modules.study.dto.DestaqueResponse;
import com.example.foundation.modules.user.domain.Usuario;

public final class DestaqueMapper {

    private DestaqueMapper() {
    }

    public static DestaqueResponse toResponse(Destaque entity) {
        return new DestaqueResponse(
                entity.getId(),
                entity.getTipo(),
                entity.getReferenciaId(),
                entity.getTexto(),
                entity.getCor(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getSessaoEstudo() != null ? entity.getSessaoEstudo().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Destaque toEntity(DestaqueRequest request) {
        Destaque entity = new Destaque();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Destaque entity, DestaqueRequest request) {
        entity.setTipo(request.tipo());
        entity.setReferenciaId(request.referenciaId());
        entity.setTexto(request.texto());
        entity.setCor(request.cor());
    }
}