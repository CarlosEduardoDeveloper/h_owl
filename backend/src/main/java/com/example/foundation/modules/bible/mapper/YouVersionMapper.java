package com.example.foundation.modules.bible.mapper;

import java.util.ArrayList;
import java.util.List;

import com.example.foundation.modules.bible.dto.BibliaIdiomaResponse;
import com.example.foundation.modules.bible.dto.BibliaResumoResponse;
import com.example.foundation.modules.bible.dto.BibliasPaginadasResponse;
import com.example.foundation.modules.bible.dto.BibliaDetalheResponse;
import com.fasterxml.jackson.databind.JsonNode;

public final class YouVersionMapper {

    private YouVersionMapper() {
    }

    public static BibliasPaginadasResponse toBibliasPaginadas(JsonNode root) {
        List<BibliaResumoResponse> dados = new ArrayList<>();
        JsonNode dataNode = root.path("data");
        if (dataNode.isArray()) {
            dataNode.forEach(item -> dados.add(toBibliaResumo(item)));
        }
        String proximoPageToken = textOrNull(root, "next_page_token");
        return new BibliasPaginadasResponse(dados, proximoPageToken);
    }

    public static BibliaResumoResponse toBibliaResumo(JsonNode item) {
        JsonNode language = item.path("language");
        BibliaIdiomaResponse idioma = language.isMissingNode() || language.isNull()
                ? null
                : new BibliaIdiomaResponse(
                        textOrNull(language, "iso_639_1"),
                        textOrNull(language, "name")
                );

        return new BibliaResumoResponse(
                item.path("id").isNumber() ? item.path("id").intValue() : null,
                textOrNull(item, "abbreviation"),
                textOrNull(item, "title"),
                idioma
        );
    }

    public static BibliaDetalheResponse toDetalhe(JsonNode root) {
        JsonNode data = root.path("data");
        if (data.isMissingNode() || data.isNull()) {
            return new BibliaDetalheResponse(root);
        }
        return new BibliaDetalheResponse(data);
    }

    private static String textOrNull(JsonNode node, String field) {
        JsonNode value = node.path(field);
        if (value.isMissingNode() || value.isNull()) {
            return null;
        }
        return value.asText();
    }
}
