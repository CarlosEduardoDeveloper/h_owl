package com.example.foundation.modules.gamification.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ConquistaUsuarioResponse(
        UUID id,
        Instant conquistadaEm,
        Integer progresso,
        UUID usuarioId,
        UUID conquistaId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}