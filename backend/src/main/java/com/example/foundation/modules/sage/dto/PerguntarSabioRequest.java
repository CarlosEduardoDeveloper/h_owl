package com.example.foundation.modules.sage.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record PerguntarSabioRequest(
        @NotBlank String pergunta,
        UUID sessaoEstudoId,
        String contextoReferencia
) {
}
