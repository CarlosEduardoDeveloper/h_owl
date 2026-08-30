package com.example.foundation.modules.study.mapper;

import com.example.foundation.modules.study.domain.Nota;
import com.example.foundation.modules.study.dto.NotaRequest;
import com.example.foundation.modules.study.dto.NotaResponse;
import com.example.foundation.modules.user.domain.Usuario;

public final class NotaMapper {

    private NotaMapper() {
    }

    public static NotaResponse toResponse(Nota entity) {
        return new NotaResponse(
                entity.getId(),
                entity.getTitulo(),
                entity.getConteudo(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getSessaoEstudo() != null ? entity.getSessaoEstudo().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Nota toEntity(NotaRequest request) {
        Nota entity = new Nota();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Nota entity, NotaRequest request) {
        entity.setTitulo(request.titulo());
        entity.setConteudo(request.conteudo());
    }
}