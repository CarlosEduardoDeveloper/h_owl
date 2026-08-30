package com.example.foundation.modules.gamification.dto;

import com.example.foundation.modules.gamification.domain.enums.TipoRecompensa;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record RecompensaRequest(
        TipoRecompensa tipo,
        String titulo,
        String descricao,
        Instant concedidaEm,
        UUID usuarioId,
        UUID corujaId,
        UUID sessaoEstudoId,
        UUID ovoUsuarioId
) {
}