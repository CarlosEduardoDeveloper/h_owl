package com.example.foundation.modules.gamification.dto;

import com.example.foundation.modules.gamification.domain.enums.TipoDecoracao;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record DecoracaoViveiroRequest(
        String nome,
        TipoDecoracao tipo,
        String imagemUrl,
        Instant adquiridaEm,
        UUID viveiroId
) {
}