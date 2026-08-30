package com.example.foundation.modules.quiz.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record RespostaQuizItemRequest(
        @NotNull UUID questaoId,
        @NotNull UUID alternativaId
) {
}
