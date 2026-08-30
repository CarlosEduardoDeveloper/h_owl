package com.example.foundation.shared.exception;

import java.util.UUID;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, UUID id) {
        super(recurso + " não encontrado: " + id);
    }
}
