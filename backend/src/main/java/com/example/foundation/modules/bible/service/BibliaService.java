package com.example.foundation.modules.bible.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.foundation.modules.bible.client.YouVersionClient;
import com.example.foundation.modules.bible.dto.BibliaDetalheResponse;
import com.example.foundation.modules.bible.dto.BibliasPaginadasResponse;
import com.example.foundation.modules.bible.mapper.YouVersionMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@Transactional(readOnly = true)
public class BibliaService {

    private final YouVersionClient youVersionClient;

    public BibliaService(YouVersionClient youVersionClient) {
        this.youVersionClient = youVersionClient;
    }

    public BibliasPaginadasResponse listar(String idioma, Integer pageSize, String pageToken) {
        JsonNode resposta = youVersionClient.listarBiblias(idioma, pageSize, pageToken);
        return YouVersionMapper.toBibliasPaginadas(resposta);
    }

    public BibliaDetalheResponse buscarPorId(int bibleId) {
        JsonNode resposta = youVersionClient.buscarBiblia(bibleId);
        return YouVersionMapper.toDetalhe(resposta);
    }

    public BibliaDetalheResponse listarLivros(int bibleId) {
        JsonNode resposta = youVersionClient.listarLivros(bibleId);
        return YouVersionMapper.toDetalhe(resposta);
    }

    public BibliaDetalheResponse buscarPassagem(int bibleId, String referenciaUsfm) {
        JsonNode resposta = youVersionClient.buscarPassagem(bibleId, referenciaUsfm);
        return YouVersionMapper.toDetalhe(resposta);
    }

    public BibliaDetalheResponse listarVersiculos(int bibleId, String livroUsfm, int capitulo) {
        JsonNode resposta = youVersionClient.listarVersiculos(bibleId, livroUsfm, capitulo);
        return YouVersionMapper.toDetalhe(resposta);
    }
}
