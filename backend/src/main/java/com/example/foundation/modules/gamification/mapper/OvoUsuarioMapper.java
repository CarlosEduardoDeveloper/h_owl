package com.example.foundation.modules.gamification.mapper;

import com.example.foundation.modules.gamification.domain.OvoUsuario;
import com.example.foundation.modules.gamification.dto.OvoUsuarioRequest;
import com.example.foundation.modules.gamification.dto.OvoUsuarioResponse;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.user.domain.Usuario;

public final class OvoUsuarioMapper {

    private OvoUsuarioMapper() {
    }

    public static OvoUsuarioResponse toResponse(OvoUsuario entity) {
        return new OvoUsuarioResponse(
                entity.getId(),
                entity.getStatus(),
                entity.getChocadoEm(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getTipoOvo() != null ? entity.getTipoOvo().getId() : null,
                entity.getSessaoEstudo() != null ? entity.getSessaoEstudo().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static OvoUsuario toEntity(OvoUsuarioRequest request) {
        OvoUsuario entity = new OvoUsuario();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(OvoUsuario entity, OvoUsuarioRequest request) {
        entity.setStatus(request.status());
        entity.setChocadoEm(request.chocadoEm());
    }
}