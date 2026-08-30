package com.example.foundation.modules.gamification.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ConquistaResponse(
        UUID id,
        String nome,
        String descricao,
        String iconeUrl,
        String categoria,
        String regra,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}