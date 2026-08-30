package com.example.foundation.modules.sage.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ConsultaSabioRequest(
        String pergunta,
        String resposta,
        String contextoReferencia,
        UUID usuarioId,
        UUID sessaoEstudoId
) {
}