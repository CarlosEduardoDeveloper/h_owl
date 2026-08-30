package com.example.foundation.modules.learning.dto;

import com.example.foundation.modules.learning.domain.enums.ProgressoTrilhaStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ProgressoTrilhaRequest(
        ProgressoTrilhaStatus status,
        Integer progressoPercentual,
        Instant ultimoAcessoEm,
        Instant concluidoEm,
        UUID usuarioId,
        UUID trilhaId,
        UUID moduloAtualId
) {
}