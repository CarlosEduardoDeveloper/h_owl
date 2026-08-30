package com.example.foundation.modules.user.mapper;

import com.example.foundation.modules.user.domain.PreferenciaUsuario;
import com.example.foundation.modules.user.dto.PreferenciaUsuarioRequest;
import com.example.foundation.modules.user.dto.PreferenciaUsuarioResponse;

public final class PreferenciaUsuarioMapper {

    private PreferenciaUsuarioMapper() {
    }

    public static PreferenciaUsuarioResponse toResponse(PreferenciaUsuario entity) {
        return new PreferenciaUsuarioResponse(
                entity.getId(),
                entity.getTema(),
                entity.getModoFocoPadrao(),
                entity.getNotificacoesAtivas(),
                entity.getDuracaoFocoPadrao(),
                entity.getVersaoBibliaPreferida(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static PreferenciaUsuario toEntity(PreferenciaUsuarioRequest request) {
        PreferenciaUsuario entity = new PreferenciaUsuario();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(PreferenciaUsuario entity, PreferenciaUsuarioRequest request) {
        entity.setTema(request.tema());
        entity.setModoFocoPadrao(request.modoFocoPadrao());
        entity.setNotificacoesAtivas(request.notificacoesAtivas());
        entity.setDuracaoFocoPadrao(request.duracaoFocoPadrao());
        entity.setVersaoBibliaPreferida(request.versaoBibliaPreferida());
    }
}