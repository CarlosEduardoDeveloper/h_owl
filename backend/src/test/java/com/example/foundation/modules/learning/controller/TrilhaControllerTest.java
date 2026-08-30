package com.example.foundation.modules.learning.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.foundation.modules.learning.dto.TrilhaRequest;
import com.example.foundation.modules.learning.dto.TrilhaResponse;
import com.example.foundation.modules.learning.service.TrilhaService;
import com.example.foundation.shared.exception.GlobalExceptionHandler;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TrilhaController.class)
@Import(GlobalExceptionHandler.class)
class TrilhaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrilhaService trilhaService;

    @Test
    void cadastrarRetorna201() throws Exception {
        UUID id = UUID.randomUUID();
        TrilhaResponse response = new TrilhaResponse(
                id, "Atributos de Deus", "Descricao", null, null, null,
                true, Instant.parse("2026-01-01T00:00:00Z"), null, null
        );
        when(trilhaService.criar(any(TrilhaRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/trilhas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Atributos de Deus","descricao":"Descricao"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.titulo").value("Atributos de Deus"));
    }

    @Test
    void listarRetorna200() throws Exception {
        UUID id = UUID.randomUUID();
        when(trilhaService.listarAtivos()).thenReturn(List.of(
                new TrilhaResponse(id, "Trilha", null, null, null, null, true, Instant.now(), null, null)
        ));

        mockMvc.perform(get("/api/v1/trilhas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()));
    }

    @Test
    void buscarRetorna200() throws Exception {
        UUID id = UUID.randomUUID();
        when(trilhaService.buscarAtivo(id)).thenReturn(
                new TrilhaResponse(id, "Trilha", null, null, null, null, true, Instant.now(), null, null)
        );

        mockMvc.perform(get("/api/v1/trilhas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Trilha"));
    }

    @Test
    void buscarInexistenteRetorna404() throws Exception {
        UUID id = UUID.randomUUID();
        when(trilhaService.buscarAtivo(id)).thenThrow(new RecursoNaoEncontradoException("Trilha", id));

        mockMvc.perform(get("/api/v1/trilhas/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarRetorna200() throws Exception {
        UUID id = UUID.randomUUID();
        when(trilhaService.atualizar(eq(id), any(TrilhaRequest.class))).thenReturn(
                new TrilhaResponse(id, "Atualizada", null, null, null, null, true, Instant.now(), Instant.now(), null)
        );

        mockMvc.perform(put("/api/v1/trilhas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Atualizada"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Atualizada"));
    }

    @Test
    void excluirRetorna204() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(trilhaService).excluir(id);

        mockMvc.perform(delete("/api/v1/trilhas/{id}", id))
                .andExpect(status().isNoContent());

        verify(trilhaService).excluir(id);
    }
}
