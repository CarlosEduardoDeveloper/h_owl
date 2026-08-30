package com.example.foundation.modules.quiz.dto;

import java.util.List;
import java.util.UUID;

public record AlternativaJogarResponse(
        UUID id,
        String texto,
        Integer ordem
) {
}
