package com.example.foundation.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistrarRequest(
        @NotBlank String usuario,
        @NotBlank String senha
) {
}
