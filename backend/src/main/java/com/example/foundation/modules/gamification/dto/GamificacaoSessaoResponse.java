package com.example.foundation.modules.gamification.dto;

import java.util.UUID;

import com.example.foundation.modules.gamification.domain.enums.SaudeFloresta;

public record GamificacaoSessaoResponse(
        UUID ovoId,
        UUID corujaUsuarioId,
        String corujaNome,
        Integer poleiroIndice,
        boolean biscoitoConcedido,
        int saldoBiscoitos,
        Integer streakAtual,
        SaudeFloresta saudeFloresta
) {
}
