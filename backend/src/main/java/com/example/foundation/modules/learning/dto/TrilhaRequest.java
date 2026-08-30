package com.example.foundation.modules.learning.dto;

import com.example.foundation.modules.learning.domain.enums.NivelDificuldade;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TrilhaRequest(
        String titulo,
        String descricao,
        String imagemUrl,
        NivelDificuldade nivelDificuldade,
        Integer ordem
) {
}