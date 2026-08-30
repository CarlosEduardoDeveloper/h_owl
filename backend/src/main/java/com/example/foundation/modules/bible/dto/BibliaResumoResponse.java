package com.example.foundation.modules.bible.dto;

public record BibliaResumoResponse(
        Integer id,
        String abreviacao,
        String titulo,
        BibliaIdiomaResponse idioma
) {
}
