package com.example.foundation.modules.gamification.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ConquistaUsuarioRequest(
        Instant conquistadaEm,
        Integer progresso,
        UUID usuarioId,
        UUID conquistaId
) {
}