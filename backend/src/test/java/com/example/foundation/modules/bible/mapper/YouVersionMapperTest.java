package com.example.foundation.modules.bible.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.foundation.modules.bible.dto.BibliasPaginadasResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class YouVersionMapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void mapeiaListaDeBiblias() throws Exception {
        var root = objectMapper.readTree("""
                {
                  "data": [
                    {
                      "id": 3034,
                      "abbreviation": "BSB",
                      "title": "Berean Standard Bible",
                      "language": { "iso_639_1": "en", "name": "English" }
                    }
                  ],
                  "next_page_token": "abc"
                }
                """);

        BibliasPaginadasResponse response = YouVersionMapper.toBibliasPaginadas(root);

        assertThat(response.dados()).hasSize(1);
        assertThat(response.dados().getFirst().id()).isEqualTo(3034);
        assertThat(response.dados().getFirst().abreviacao()).isEqualTo("BSB");
        assertThat(response.dados().getFirst().idioma().iso6391()).isEqualTo("en");
        assertThat(response.proximoPageToken()).isEqualTo("abc");
    }
}
