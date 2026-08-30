package com.example.foundation.modules.gamification.dto;

import com.example.foundation.modules.gamification.domain.enums.OvoStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record OvoUsuarioResponse(
        UUID id,
        OvoStatus status,
        Instant chocadoEm,
        UUID usuarioId,
        UUID tipoOvoId,
        UUID sessaoEstudoId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}