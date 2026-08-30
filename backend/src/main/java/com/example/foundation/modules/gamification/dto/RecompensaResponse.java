package com.example.foundation.modules.gamification.dto;

import com.example.foundation.modules.gamification.domain.enums.TipoRecompensa;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record RecompensaResponse(
        UUID id,
        TipoRecompensa tipo,
        String titulo,
        String descricao,
        Instant concedidaEm,
        UUID usuarioId,
        UUID corujaId,
        UUID sessaoEstudoId,
        UUID ovoUsuarioId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}