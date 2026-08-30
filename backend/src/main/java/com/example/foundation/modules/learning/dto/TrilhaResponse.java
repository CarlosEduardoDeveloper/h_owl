package com.example.foundation.modules.learning.dto;

import com.example.foundation.modules.learning.domain.enums.NivelDificuldade;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TrilhaResponse(
        UUID id,
        String titulo,
        String descricao,
        String imagemUrl,
        NivelDificuldade nivelDificuldade,
        Integer ordem,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}