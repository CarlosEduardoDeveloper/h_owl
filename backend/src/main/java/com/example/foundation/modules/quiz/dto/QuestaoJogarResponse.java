package com.example.foundation.modules.quiz.dto;

import java.util.List;
import java.util.UUID;

public record QuestaoJogarResponse(
        UUID id,
        String enunciado,
        Integer ordem,
        List<AlternativaJogarResponse> alternativas
) {
}
