package com.example.foundation.modules.quiz.dto;

import java.util.List;

import com.example.foundation.modules.gamification.domain.enums.SaudeFloresta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record FinalizarTentativaQuizRequest(
        @NotEmpty @Valid List<RespostaQuizItemRequest> respostas
) {
}
