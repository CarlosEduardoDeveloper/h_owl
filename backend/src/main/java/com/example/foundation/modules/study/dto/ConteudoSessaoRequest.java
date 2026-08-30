package com.example.foundation.modules.study.dto;

import com.example.foundation.modules.study.domain.enums.TipoConteudoSessao;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ConteudoSessaoRequest(
        TipoConteudoSessao tipo,
        UUID referenciaId,
        String titulo,
        String descricao,
        Integer ordem,
        UUID sessaoEstudoId
) {
}