package com.example.foundation.modules.gamification.dto;

import com.example.foundation.modules.gamification.domain.enums.Raridade;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CorujaResponse(
        UUID id,
        String nome,
        String especie,
        Raridade raridade,
        String descricao,
        String imagemUrl,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}