package com.example.foundation.modules.auth.dto;

import java.util.UUID;

import com.example.foundation.modules.user.domain.enums.UsuarioStatus;

public record SessaoResponse(
        UUID usuarioId,
        String usuario,
        UsuarioStatus status
) {
}
