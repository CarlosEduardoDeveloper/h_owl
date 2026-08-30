package com.example.foundation.modules.learning.dto;

import com.example.foundation.modules.learning.domain.enums.TipoConteudo;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ConteudoResponse(
        UUID id,
        TipoConteudo tipo,
        String titulo,
        String conteudo,
        String url,
        Integer ordem,
        UUID moduloId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}