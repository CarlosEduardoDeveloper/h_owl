package com.example.foundation.modules.gamification.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ViveiroResponse(
        UUID id,
        String nome,
        Integer nivel,
        Long xpTotal,
        String temaVisual,
        UUID usuarioId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}