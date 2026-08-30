package com.example.foundation.modules.user.dto;

import com.example.foundation.modules.user.domain.enums.TemaAplicacao;
import com.example.foundation.shared.domain.enums.ModoFoco;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record PreferenciaUsuarioResponse(
        UUID id,
        TemaAplicacao tema,
        ModoFoco modoFocoPadrao,
        Boolean notificacoesAtivas,
        Integer duracaoFocoPadrao,
        String versaoBibliaPreferida,
        UUID usuarioId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}