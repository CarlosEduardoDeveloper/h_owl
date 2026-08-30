package com.example.foundation.modules.study.mapper;

import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.dto.SessaoEstudoRequest;
import com.example.foundation.modules.study.dto.SessaoEstudoResponse;
import com.example.foundation.modules.user.domain.Usuario;

public final class SessaoEstudoMapper {

    private SessaoEstudoMapper() {
    }

    public static SessaoEstudoResponse toResponse(SessaoEstudo entity) {
        return new SessaoEstudoResponse(
                entity.getId(),
                entity.getIntencao(),
                entity.getModoFoco(),
                entity.getDuracaoPlanejadaMinutos(),
                entity.getDuracaoRealMinutos(),
                entity.getInicioEm(),
                entity.getFimEm(),
                entity.getStatus(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static SessaoEstudo toEntity(SessaoEstudoRequest request) {
        SessaoEstudo entity = new SessaoEstudo();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(SessaoEstudo entity, SessaoEstudoRequest request) {
        entity.setIntencao(request.intencao());
        entity.setModoFoco(request.modoFoco());
        entity.setDuracaoPlanejadaMinutos(request.duracaoPlanejadaMinutos());
        entity.setDuracaoRealMinutos(request.duracaoRealMinutos());
        entity.setInicioEm(request.inicioEm());
        entity.setFimEm(request.fimEm());
        entity.setStatus(request.status());
    }
}