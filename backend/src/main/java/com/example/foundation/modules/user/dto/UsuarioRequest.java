package com.example.foundation.modules.user.dto;

import com.example.foundation.modules.user.domain.enums.UsuarioStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record UsuarioRequest(
        String email,
        String senhaHash,
        UsuarioStatus status,
        UUID pessoaId
) {
}