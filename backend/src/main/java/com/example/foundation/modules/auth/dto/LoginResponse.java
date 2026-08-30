package com.example.foundation.modules.auth.dto;

import java.util.UUID;

public record LoginResponse(
        UUID usuarioId,
        String usuario
) {
}
