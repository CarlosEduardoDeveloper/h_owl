package com.example.foundation.modules.bible.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(YouVersionCallbackController.class)
class YouVersionCallbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void callbackSemParametrosRetornaVerifyingSession() throws Exception {
        mockMvc.perform(get("/callback"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Verifying session...")));
    }

    @Test
    void callbackComCodeRetornaConcluido() throws Exception {
        mockMvc.perform(get("/callback").param("code", "abc123"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Autenticação concluída")));
    }
}
