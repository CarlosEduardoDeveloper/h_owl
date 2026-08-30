package com.example.foundation.modules.quiz.mapper;

import com.example.foundation.modules.quiz.domain.RespostaQuestao;
import com.example.foundation.modules.quiz.dto.RespostaQuestaoRequest;
import com.example.foundation.modules.quiz.dto.RespostaQuestaoResponse;

public final class RespostaQuestaoMapper {

    private RespostaQuestaoMapper() {
    }

    public static RespostaQuestaoResponse toResponse(RespostaQuestao entity) {
        return new RespostaQuestaoResponse(
                entity.getId(),
                entity.getCorreta(),
                entity.getTentativaQuiz() != null ? entity.getTentativaQuiz().getId() : null,
                entity.getQuestao() != null ? entity.getQuestao().getId() : null,
                entity.getAlternativa() != null ? entity.getAlternativa().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static RespostaQuestao toEntity(RespostaQuestaoRequest request) {
        RespostaQuestao entity = new RespostaQuestao();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(RespostaQuestao entity, RespostaQuestaoRequest request) {
        entity.setCorreta(request.correta());
    }
}