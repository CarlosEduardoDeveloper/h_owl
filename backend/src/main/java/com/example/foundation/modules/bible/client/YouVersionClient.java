package com.example.foundation.modules.bible.client;

import com.example.foundation.config.YouVersionProperties;
import com.example.foundation.shared.exception.YouVersionIntegrationException;
import com.example.foundation.shared.exception.YouVersionNaoConfiguradoException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class YouVersionClient {

    private static final String APP_KEY_HEADER = "X-YVP-App-Key";

    private final RestClient restClient;
    private final YouVersionProperties properties;

    public YouVersionClient(RestClient youVersionRestClient, YouVersionProperties properties) {
        this.restClient = youVersionRestClient;
        this.properties = properties;
    }

    public JsonNode listarBiblias(String idioma, Integer pageSize, String pageToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/bibles");
        if (idioma != null && !idioma.isBlank()) {
            builder.queryParam("language_ranges[]", idioma);
        }
        if (pageSize != null) {
            builder.queryParam("page_size", pageSize);
        }
        if (pageToken != null && !pageToken.isBlank()) {
            builder.queryParam("page_token", pageToken);
        }
        return get(builder.toUriString());
    }

    public JsonNode buscarBiblia(int bibleId) {
        return get("/bibles/" + bibleId);
    }

    public JsonNode listarLivros(int bibleId) {
        return get("/bibles/" + bibleId + "/books");
    }

    public JsonNode buscarPassagem(int bibleId, String referenciaUsfm) {
        return get("/bibles/" + bibleId + "/passages/" + referenciaUsfm);
    }

    public JsonNode listarVersiculos(int bibleId, String livroUsfm, int capitulo) {
        return get("/bibles/" + bibleId + "/books/" + livroUsfm + "/chapters/" + capitulo + "/verses");
    }

    private JsonNode get(String uri) {
        validarConfiguracao();

        return restClient.get()
                .uri(uri)
                .header(APP_KEY_HEADER, properties.appKey())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new YouVersionIntegrationException(
                            response.getStatusCode().value(),
                            "YouVersion retornou HTTP " + response.getStatusCode().value()
                    );
                })
                .body(JsonNode.class);
    }

    private void validarConfiguracao() {
        if (!properties.isConfigured()) {
            throw new YouVersionNaoConfiguradoException();
        }
    }
}
