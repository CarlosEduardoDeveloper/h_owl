package com.example.foundation.modules.quiz.dto;

import java.util.List;
import java.util.UUID;

public record QuizJogarResponse(
        UUID id,
        String titulo,
        String descricao,
        List<QuestaoJogarResponse> questoes
) {
}
