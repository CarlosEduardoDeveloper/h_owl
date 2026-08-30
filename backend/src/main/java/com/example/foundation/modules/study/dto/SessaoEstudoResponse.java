package com.example.foundation.modules.study.dto;

import com.example.foundation.modules.study.domain.enums.IntencaoEstudo;
import com.example.foundation.modules.study.domain.enums.SessaoEstudoStatus;
import com.example.foundation.shared.domain.enums.ModoFoco;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record SessaoEstudoResponse(
        UUID id,
        IntencaoEstudo intencao,
        ModoFoco modoFoco,
        Integer duracaoPlanejadaMinutos,
        Integer duracaoRealMinutos,
        Instant inicioEm,
        Instant fimEm,
        SessaoEstudoStatus status,
        UUID usuarioId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}