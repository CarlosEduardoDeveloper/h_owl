package com.example.foundation.modules.study.dto;

import com.example.foundation.modules.gamification.dto.GamificacaoSessaoResponse;
import com.example.foundation.modules.study.domain.enums.IntencaoEstudo;
import com.example.foundation.modules.study.domain.enums.SessaoEstudoStatus;
import com.example.foundation.shared.domain.enums.ModoFoco;
import java.time.Instant;
import java.util.UUID;

public record SessaoEstudoConclusaoResponse(
        UUID id,
        IntencaoEstudo intencao,
        ModoFoco modoFoco,
        Integer duracaoPlanejadaMinutos,
        Integer duracaoRealMinutos,
        Instant inicioEm,
        Instant fimEm,
        SessaoEstudoStatus status,
        UUID usuarioId,
        GamificacaoSessaoResponse gamificacao
) {
    public static SessaoEstudoConclusaoResponse from(
            SessaoEstudoResponse sessao,
            GamificacaoSessaoResponse gamificacao
    ) {
        return new SessaoEstudoConclusaoResponse(
                sessao.id(),
                sessao.intencao(),
                sessao.modoFoco(),
                sessao.duracaoPlanejadaMinutos(),
                sessao.duracaoRealMinutos(),
                sessao.inicioEm(),
                sessao.fimEm(),
                sessao.status(),
                sessao.usuarioId(),
                gamificacao
        );
    }
}
