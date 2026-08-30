package com.example.foundation.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrarRequest(
        @NotBlank @Size(max = 320) String usuario,
        @NotBlank @Size(min = 1, max = 255) String senha
) {
}
