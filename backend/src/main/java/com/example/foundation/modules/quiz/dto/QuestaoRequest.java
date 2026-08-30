package com.example.foundation.modules.quiz.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record QuestaoRequest(
        String enunciado,
        Integer ordem,
        UUID quizId
) {
}