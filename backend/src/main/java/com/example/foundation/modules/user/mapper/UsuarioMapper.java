package com.example.foundation.modules.user.mapper;

import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.dto.UsuarioRequest;
import com.example.foundation.modules.user.dto.UsuarioResponse;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioResponse toResponse(Usuario entity) {
        return new UsuarioResponse(
                entity.getId(),
                entity.getEmail(),
                entity.getSenhaHash(),
                entity.getStatus(),
                entity.getPessoa() != null ? entity.getPessoa().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Usuario toEntity(UsuarioRequest request) {
        Usuario entity = new Usuario();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Usuario entity, UsuarioRequest request) {
        entity.setEmail(request.email());
        entity.setSenhaHash(request.senhaHash());
        entity.setStatus(request.status());
    }
}