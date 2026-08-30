package com.example.foundation.modules.gamification.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ConquistaRequest(
        String nome,
        String descricao,
        String iconeUrl,
        String categoria,
        String regra
) {
}