package com.example.foundation.modules.gamification.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CorujaUsuarioResponse(
        UUID id,
        Instant adquiridaEm,
        Integer nivel,
        Integer experiencia,
        String observacoes,
        UUID usuarioId,
        UUID corujaId,
        UUID sessaoEstudoId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}