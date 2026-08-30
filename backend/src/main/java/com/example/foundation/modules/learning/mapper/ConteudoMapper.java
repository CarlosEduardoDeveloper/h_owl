package com.example.foundation.modules.learning.mapper;

import com.example.foundation.modules.learning.domain.Conteudo;
import com.example.foundation.modules.learning.dto.ConteudoRequest;
import com.example.foundation.modules.learning.dto.ConteudoResponse;

public final class ConteudoMapper {

    private ConteudoMapper() {
    }

    public static ConteudoResponse toResponse(Conteudo entity) {
        return new ConteudoResponse(
                entity.getId(),
                entity.getTipo(),
                entity.getTitulo(),
                entity.getConteudo(),
                entity.getUrl(),
                entity.getOrdem(),
                entity.getModulo() != null ? entity.getModulo().getId() : null,
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Conteudo toEntity(ConteudoRequest request) {
        Conteudo entity = new Conteudo();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Conteudo entity, ConteudoRequest request) {
        entity.setTipo(request.tipo());
        entity.setTitulo(request.titulo());
        entity.setConteudo(request.conteudo());
        entity.setUrl(request.url());
        entity.setOrdem(request.ordem());
    }
}