package com.example.foundation.modules.quiz.dto;

import java.util.UUID;

public record IniciarTentativaQuizResponse(
        UUID tentativaId,
        UUID quizId,
        QuizJogarResponse quiz
) {
}
