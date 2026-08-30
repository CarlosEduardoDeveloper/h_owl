package com.example.foundation.modules.user.dto;

import java.util.UUID;

import com.example.foundation.modules.gamification.domain.enums.OvoStatus;
import com.example.foundation.modules.study.domain.enums.IntencaoEstudo;
import com.example.foundation.modules.study.domain.enums.SessaoEstudoStatus;

public record MeResumoResponse(
        UUID usuarioId,
        String usuario,
        Integer ofensiva,
        Long xpDiario,
        Integer ranking,
        MeViveiroResumo viveiro,
        MeOvoResumo ovoAtivo,
        MeSessaoResumo sessaoAtual,
        java.util.List<MeTrilhaProgressoResumo> trilhasEmProgresso
) {

    public record MeViveiroResumo(
            UUID id,
            String nome,
            Integer nivel,
            Long xpTotal
    ) {
    }

    public record MeOvoResumo(
            UUID id,
            OvoStatus status
    ) {
    }

    public record MeSessaoResumo(
            UUID id,
            SessaoEstudoStatus status,
            IntencaoEstudo intencao
    ) {
    }

    public record MeTrilhaProgressoResumo(
            UUID trilhaId,
            String titulo,
            Integer progressoPercentual
    ) {
    }
}
