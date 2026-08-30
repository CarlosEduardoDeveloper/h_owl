package com.example.foundation.modules.quiz.mapper;

import com.example.foundation.modules.quiz.domain.TentativaQuiz;
import com.example.foundation.modules.quiz.dto.TentativaQuizRequest;
import com.example.foundation.modules.quiz.dto.TentativaQuizResponse;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.user.domain.Usuario;

public final class TentativaQuizMapper {

    private TentativaQuizMapper() {
    }

    public static TentativaQuizResponse toResponse(TentativaQuiz entity) {
        return new TentativaQuizResponse(
                entity.getId(),
                entity.getPontuacao(),
                entity.getAcertos(),
                entity.getTotalQuestoes(),
                entity.getRealizadoEm(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getQuiz() != null ? entity.getQuiz().getId() : null,
                entity.getSessaoEstudo() != null ? entity.getSessaoEstudo().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static TentativaQuiz toEntity(TentativaQuizRequest request) {
        TentativaQuiz entity = new TentativaQuiz();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(TentativaQuiz entity, TentativaQuizRequest request) {
        entity.setPontuacao(request.pontuacao());
        entity.setAcertos(request.acertos());
        entity.setTotalQuestoes(request.totalQuestoes());
        entity.setRealizadoEm(request.realizadoEm());
    }
}