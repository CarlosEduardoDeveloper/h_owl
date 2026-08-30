package com.example.foundation.modules.study.dto;

import com.example.foundation.modules.study.domain.enums.TipoDestaque;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record DestaqueResponse(
        UUID id,
        TipoDestaque tipo,
        UUID referenciaId,
        String texto,
        String cor,
        UUID usuarioId,
        UUID sessaoEstudoId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}