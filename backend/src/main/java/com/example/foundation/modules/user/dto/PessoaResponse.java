package com.example.foundation.modules.user.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record PessoaResponse(
        UUID id,
        String nome,
        LocalDate dataNascimento,
        String genero,
        String fotoUrl,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}