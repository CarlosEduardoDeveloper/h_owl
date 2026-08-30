package com.example.foundation.modules.quiz.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record QuizRequest(
        String titulo,
        String descricao,
        Integer ordem,
        UUID moduloId
) {
}