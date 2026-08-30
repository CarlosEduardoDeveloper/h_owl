package com.example.foundation.modules.bible.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.foundation.modules.bible.dto.BibliaIdiomaResponse;
import com.example.foundation.modules.bible.dto.BibliaResumoResponse;
import com.example.foundation.modules.bible.dto.BibliasPaginadasResponse;
import com.example.foundation.modules.bible.service.BibliaService;
import com.example.foundation.shared.exception.GlobalExceptionHandler;
import com.example.foundation.shared.exception.YouVersionNaoConfiguradoException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BibliaController.class)
@Import(GlobalExceptionHandler.class)
class BibliaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BibliaService bibliaService;

    @Test
    void listarRetorna200() throws Exception {
        when(bibliaService.listar("por", null, null)).thenReturn(new BibliasPaginadasResponse(
                List.of(new BibliaResumoResponse(
                        3034,
                        "BSB",
                        "Berean Standard Bible",
                        new BibliaIdiomaResponse("en", "English")
                )),
                null
        ));

        mockMvc.perform(get("/api/v1/biblias").param("idioma", "por"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados[0].id").value(3034))
                .andExpect(jsonPath("$.dados[0].abreviacao").value("BSB"));
    }

    @Test
    void listarSemConfiguracaoRetorna503() throws Exception {
        when(bibliaService.listar(null, null, null))
                .thenThrow(new YouVersionNaoConfiguradoException());

        mockMvc.perform(get("/api/v1/biblias"))
                .andExpect(status().isServiceUnavailable());
    }
}
