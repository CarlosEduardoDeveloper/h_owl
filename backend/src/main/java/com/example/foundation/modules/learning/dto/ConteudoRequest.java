package com.example.foundation.modules.learning.dto;

import com.example.foundation.modules.learning.domain.enums.TipoConteudo;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ConteudoRequest(
        TipoConteudo tipo,
        String titulo,
        String conteudo,
        String url,
        Integer ordem,
        UUID moduloId
) {
}