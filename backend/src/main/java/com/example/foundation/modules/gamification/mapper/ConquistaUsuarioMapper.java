package com.example.foundation.modules.gamification.mapper;

import com.example.foundation.modules.gamification.domain.ConquistaUsuario;
import com.example.foundation.modules.gamification.dto.ConquistaUsuarioRequest;
import com.example.foundation.modules.gamification.dto.ConquistaUsuarioResponse;
import com.example.foundation.modules.user.domain.Usuario;

public final class ConquistaUsuarioMapper {

    private ConquistaUsuarioMapper() {
    }

    public static ConquistaUsuarioResponse toResponse(ConquistaUsuario entity) {
        return new ConquistaUsuarioResponse(
                entity.getId(),
                entity.getConquistadaEm(),
                entity.getProgresso(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getConquista() != null ? entity.getConquista().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static ConquistaUsuario toEntity(ConquistaUsuarioRequest request) {
        ConquistaUsuario entity = new ConquistaUsuario();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(ConquistaUsuario entity, ConquistaUsuarioRequest request) {
        entity.setConquistadaEm(request.conquistadaEm());
        entity.setProgresso(request.progresso());
    }
}