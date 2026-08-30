package com.example.foundation.modules.quiz.dto;

import java.util.UUID;

import com.example.foundation.modules.gamification.domain.enums.SaudeFloresta;

public record FinalizarTentativaQuizResponse(
        UUID tentativaId,
        int acertos,
        int totalQuestoes,
        int pontuacaoPercentual,
        boolean biscoitoConcedido,
        int saldoBiscoitos,
        Integer streakAtual,
        SaudeFloresta saudeFloresta
) {
}
