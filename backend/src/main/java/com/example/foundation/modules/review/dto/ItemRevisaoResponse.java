package com.example.foundation.modules.review.dto;

import com.example.foundation.modules.review.domain.enums.TipoItemRevisao;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ItemRevisaoResponse(
        UUID id,
        TipoItemRevisao tipo,
        UUID referenciaId,
        Instant proximaRevisaoEm,
        Integer intervaloDias,
        Integer facilidade,
        Integer repeticoes,
        UUID usuarioId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}