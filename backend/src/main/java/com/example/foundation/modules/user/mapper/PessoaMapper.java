package com.example.foundation.modules.user.mapper;

import com.example.foundation.modules.user.domain.Pessoa;
import com.example.foundation.modules.user.dto.PessoaRequest;
import com.example.foundation.modules.user.dto.PessoaResponse;

public final class PessoaMapper {

    private PessoaMapper() {
    }

    public static PessoaResponse toResponse(Pessoa entity) {
        return new PessoaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDataNascimento(),
                entity.getGenero(),
                entity.getFotoUrl(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getExcluidoEm()
        );
    }

    public static Pessoa toEntity(PessoaRequest request) {
        Pessoa entity = new Pessoa();
        applyRequest(entity, request);
        return entity;
    }

    public static void applyRequest(Pessoa entity, PessoaRequest request) {
        entity.setNome(request.nome());
        entity.setDataNascimento(request.dataNascimento());
        entity.setGenero(request.genero());
        entity.setFotoUrl(request.fotoUrl());
    }
}