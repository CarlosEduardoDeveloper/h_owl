package com.example.foundation.modules.learning.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ModuloResponse(
        UUID id,
        String titulo,
        String descricao,
        Integer ordem,
        Integer tempoSugeridoMinutos,
        UUID trilhaId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}