package com.example.foundation.modules.quiz.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record RespostaQuestaoResponse(
        UUID id,
        Boolean correta,
        UUID tentativaQuizId,
        UUID questaoId,
        UUID alternativaId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}