package com.example.foundation.modules.gamification.mapper;

import com.example.foundation.modules.gamification.domain.CorujaUsuario;
import com.example.foundation.modules.gamification.dto.CorujaUsuarioRequest;
import com.example.foundation.modules.gamification.dto.CorujaUsuarioResponse;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.user.domain.Usuario;

public final class CorujaUsuarioMapper {

    private CorujaUsuarioMapper() {
    }

    public static CorujaUsuarioResponse toResponse(CorujaUsuario entity) {
        return new CorujaUsuarioResponse(
                entity.getId(),
                entity.getAdquiridaEm(),
                entity.getNivel(),
                entity.getExperiencia(),
                entity.getObservacoes(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getCoruja() != null ? entity.getCoruja().getId() : null,
                entity.getSessaoEstudo() != null ? entity.getSessaoEstudo().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static CorujaUsuario toEntity(CorujaUsuarioRequest request) {
        CorujaUsuario entity = new CorujaUsuario();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(CorujaUsuario entity, CorujaUsuarioRequest request) {
        entity.setAdquiridaEm(request.adquiridaEm());
        entity.setNivel(request.nivel());
        entity.setExperiencia(request.experiencia());
        entity.setObservacoes(request.observacoes());
    }
}