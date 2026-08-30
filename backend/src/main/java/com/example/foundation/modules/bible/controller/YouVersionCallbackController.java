package com.example.foundation.modules.bible.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YouVersionCallbackController {

    @GetMapping(value = "/callback", produces = MediaType.TEXT_HTML_VALUE)
    public String callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String error_description
    ) {
        if (error != null && !error.isBlank()) {
            String descricao = error_description != null ? error_description : error;
            return paginaHtml("Erro na autenticação YouVersion", descricao);
        }
        if (code != null && !code.isBlank()) {
            return paginaHtml("Autenticação concluída", "Você pode fechar esta janela e voltar ao aplicativo.");
        }
        return paginaHtml("Verifying session...", "Aguarde enquanto validamos sua sessão.");
    }

    private String paginaHtml(String titulo, String mensagem) {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                  <meta charset="UTF-8"/>
                  <meta name="viewport" content="width=device-width, initial-scale=1"/>
                  <title>%s</title>
                  <style>
                    body { font-family: system-ui, sans-serif; margin: 2rem; text-align: center; color: #1a1a1a; }
                    p { color: #555; }
                  </style>
                </head>
                <body>
                  <h1>%s</h1>
                  <p>%s</p>
                </body>
                </html>
                """.formatted(titulo, titulo, mensagem);
    }
}
