package com.example.foundation.modules.quiz.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record AlternativaRequest(
        String texto,
        Boolean correta,
        Integer ordem,
        UUID questaoId
) {
}