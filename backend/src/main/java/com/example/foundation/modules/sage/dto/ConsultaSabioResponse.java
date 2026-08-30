package com.example.foundation.modules.sage.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ConsultaSabioResponse(
        UUID id,
        String pergunta,
        String resposta,
        String contextoReferencia,
        UUID usuarioId,
        UUID sessaoEstudoId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}