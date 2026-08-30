package com.example.foundation.modules.user.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record PessoaRequest(
        String nome,
        LocalDate dataNascimento,
        String genero,
        String fotoUrl
) {
}