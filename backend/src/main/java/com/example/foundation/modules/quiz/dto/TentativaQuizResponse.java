package com.example.foundation.modules.quiz.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TentativaQuizResponse(
        UUID id,
        Integer pontuacao,
        Integer acertos,
        Integer totalQuestoes,
        Instant realizadoEm,
        UUID usuarioId,
        UUID quizId,
        UUID sessaoEstudoId,
        Boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}