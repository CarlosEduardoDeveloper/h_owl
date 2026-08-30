package com.example.foundation.modules.quiz.mapper;

import com.example.foundation.modules.quiz.domain.Alternativa;
import com.example.foundation.modules.quiz.dto.AlternativaRequest;
import com.example.foundation.modules.quiz.dto.AlternativaResponse;

public final class AlternativaMapper {

    private AlternativaMapper() {
    }

    public static AlternativaResponse toResponse(Alternativa entity) {
        return new AlternativaResponse(
                entity.getId(),
                entity.getTexto(),
                entity.getCorreta(),
                entity.getOrdem(),
                entity.getQuestao() != null ? entity.getQuestao().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Alternativa toEntity(AlternativaRequest request) {
        Alternativa entity = new Alternativa();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Alternativa entity, AlternativaRequest request) {
        entity.setTexto(request.texto());
        entity.setCorreta(request.correta());
        entity.setOrdem(request.ordem());
    }
}