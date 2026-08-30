package com.example.foundation.modules.study.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record NotaResponse(
        UUID id,
        String titulo,
        String conteudo,
        UUID usuarioId,
        UUID sessaoEstudoId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}