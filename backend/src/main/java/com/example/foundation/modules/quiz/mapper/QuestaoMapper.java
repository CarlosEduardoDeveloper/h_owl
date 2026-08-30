package com.example.foundation.modules.quiz.mapper;

import com.example.foundation.modules.quiz.domain.Questao;
import com.example.foundation.modules.quiz.dto.QuestaoRequest;
import com.example.foundation.modules.quiz.dto.QuestaoResponse;

public final class QuestaoMapper {

    private QuestaoMapper() {
    }

    public static QuestaoResponse toResponse(Questao entity) {
        return new QuestaoResponse(
                entity.getId(),
                entity.getEnunciado(),
                entity.getOrdem(),
                entity.getQuiz() != null ? entity.getQuiz().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Questao toEntity(QuestaoRequest request) {
        Questao entity = new Questao();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Questao entity, QuestaoRequest request) {
        entity.setEnunciado(request.enunciado());
        entity.setOrdem(request.ordem());
    }
}