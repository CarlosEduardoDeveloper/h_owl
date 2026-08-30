package com.example.foundation.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @Size(max = 320) String usuario,
        @NotBlank @Size(max = 255) String senha
) {
}
