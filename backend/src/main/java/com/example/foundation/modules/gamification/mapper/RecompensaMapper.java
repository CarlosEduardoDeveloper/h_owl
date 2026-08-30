package com.example.foundation.modules.gamification.mapper;

import com.example.foundation.modules.gamification.domain.Recompensa;
import com.example.foundation.modules.gamification.dto.RecompensaRequest;
import com.example.foundation.modules.gamification.dto.RecompensaResponse;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.user.domain.Usuario;

public final class RecompensaMapper {

    private RecompensaMapper() {
    }

    public static RecompensaResponse toResponse(Recompensa entity) {
        return new RecompensaResponse(
                entity.getId(),
                entity.getTipo(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getConcedidaEm(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getCoruja() != null ? entity.getCoruja().getId() : null,
                entity.getSessaoEstudo() != null ? entity.getSessaoEstudo().getId() : null,
                entity.getOvoUsuario() != null ? entity.getOvoUsuario().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Recompensa toEntity(RecompensaRequest request) {
        Recompensa entity = new Recompensa();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Recompensa entity, RecompensaRequest request) {
        entity.setTipo(request.tipo());
        entity.setTitulo(request.titulo());
        entity.setDescricao(request.descricao());
        entity.setConcedidaEm(request.concedidaEm());
    }
}