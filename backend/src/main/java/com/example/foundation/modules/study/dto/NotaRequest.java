package com.example.foundation.modules.study.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record NotaRequest(
        String titulo,
        String conteudo,
        UUID usuarioId,
        UUID sessaoEstudoId
) {
}