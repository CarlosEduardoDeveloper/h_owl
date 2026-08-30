package com.example.foundation.modules.learning.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ModuloRequest(
        String titulo,
        String descricao,
        Integer ordem,
        Integer tempoSugeridoMinutos,
        UUID trilhaId
) {
}