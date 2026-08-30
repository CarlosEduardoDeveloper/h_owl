package com.example.foundation.modules.bible.controller;

import com.example.foundation.modules.bible.dto.BibliaDetalheResponse;
import com.example.foundation.modules.bible.dto.BibliasPaginadasResponse;
import com.example.foundation.modules.bible.service.BibliaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/biblias")
public class BibliaController {

    private final BibliaService bibliaService;

    public BibliaController(BibliaService bibliaService) {
        this.bibliaService = bibliaService;
    }

    /**
     * Lista versões bíblicas licenciadas para a App Key configurada.
     * Use {@code idioma=por} para português (filtro language_ranges da YouVersion).
     */
    @GetMapping
    public BibliasPaginadasResponse listar(
            @RequestParam(required = false) String idioma,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String pageToken
    ) {
        return bibliaService.listar(idioma, pageSize, pageToken);
    }

    @GetMapping("/{bibleId}")
    public BibliaDetalheResponse buscar(@PathVariable int bibleId) {
        return bibliaService.buscarPorId(bibleId);
    }

    @GetMapping("/{bibleId}/livros")
    public BibliaDetalheResponse listarLivros(@PathVariable int bibleId) {
        return bibliaService.listarLivros(bibleId);
    }

    @GetMapping("/{bibleId}/passagens/{referenciaUsfm}")
    public BibliaDetalheResponse buscarPassagem(
            @PathVariable int bibleId,
            @PathVariable String referenciaUsfm
    ) {
        return bibliaService.buscarPassagem(bibleId, referenciaUsfm);
    }

    @GetMapping("/{bibleId}/livros/{livroUsfm}/capitulos/{capitulo}/versiculos")
    public BibliaDetalheResponse listarVersiculos(
            @PathVariable int bibleId,
            @PathVariable String livroUsfm,
            @PathVariable int capitulo
    ) {
        return bibliaService.listarVersiculos(bibleId, livroUsfm, capitulo);
    }
}
