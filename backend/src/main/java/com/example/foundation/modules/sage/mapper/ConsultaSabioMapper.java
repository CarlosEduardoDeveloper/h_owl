package com.example.foundation.modules.sage.mapper;

import com.example.foundation.modules.sage.domain.ConsultaSabio;
import com.example.foundation.modules.sage.dto.ConsultaSabioRequest;
import com.example.foundation.modules.sage.dto.ConsultaSabioResponse;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.user.domain.Usuario;

public final class ConsultaSabioMapper {

    private ConsultaSabioMapper() {
    }

    public static ConsultaSabioResponse toResponse(ConsultaSabio entity) {
        return new ConsultaSabioResponse(
                entity.getId(),
                entity.getPergunta(),
                entity.getResposta(),
                entity.getContextoReferencia(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getSessaoEstudo() != null ? entity.getSessaoEstudo().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static ConsultaSabio toEntity(ConsultaSabioRequest request) {
        ConsultaSabio entity = new ConsultaSabio();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(ConsultaSabio entity, ConsultaSabioRequest request) {
        entity.setPergunta(request.pergunta());
        entity.setResposta(request.resposta());
        entity.setContextoReferencia(request.contextoReferencia());
    }
}