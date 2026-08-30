package com.example.foundation.modules.quiz.mapper;

import com.example.foundation.modules.learning.domain.Modulo;
import com.example.foundation.modules.quiz.domain.Quiz;
import com.example.foundation.modules.quiz.dto.QuizRequest;
import com.example.foundation.modules.quiz.dto.QuizResponse;

public final class QuizMapper {

    private QuizMapper() {
    }

    public static QuizResponse toResponse(Quiz entity) {
        return new QuizResponse(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getOrdem(),
                entity.getModulo() != null ? entity.getModulo().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Quiz toEntity(QuizRequest request) {
        Quiz entity = new Quiz();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Quiz entity, QuizRequest request) {
        entity.setTitulo(request.titulo());
        entity.setDescricao(request.descricao());
        entity.setOrdem(request.ordem());
    }
}