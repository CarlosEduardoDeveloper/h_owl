package com.example.foundation.modules.quiz.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record AlternativaResponse(
        UUID id,
        String texto,
        Boolean correta,
        Integer ordem,
        UUID questaoId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}