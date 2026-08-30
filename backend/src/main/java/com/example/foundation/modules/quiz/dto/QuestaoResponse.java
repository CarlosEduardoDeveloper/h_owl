package com.example.foundation.modules.quiz.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record QuestaoResponse(
        UUID id,
        String enunciado,
        Integer ordem,
        UUID quizId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}