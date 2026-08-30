package com.example.foundation.modules.auth.controller;

import java.util.UUID;

import com.example.foundation.modules.auth.dto.LoginRequest;
import com.example.foundation.modules.auth.dto.LoginResponse;
import com.example.foundation.modules.auth.dto.RegistrarRequest;
import com.example.foundation.modules.auth.dto.SessaoResponse;
import com.example.foundation.modules.auth.service.AuthService;
import com.example.foundation.modules.user.domain.enums.UsuarioStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void loginRetornaUsuario() throws Exception {
        UUID usuarioId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new LoginResponse(usuarioId, "joao"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuario":"joao","senha":"123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(usuarioId.toString()))
                .andExpect(jsonPath("$.usuario").value("joao"));
    }

    @Test
    void registrarRetornaCreated() throws Exception {
        UUID usuarioId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        when(authService.registrar(any(RegistrarRequest.class)))
                .thenReturn(new LoginResponse(usuarioId, "maria"));

        mockMvc.perform(post("/api/v1/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuario":"maria","senha":"123"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario").value("maria"));
    }

    @Test
    void sessaoRetornaUsuarioAutenticado() throws Exception {
        UUID usuarioId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        when(authService.buscarSessao(usuarioId))
                .thenReturn(new SessaoResponse(usuarioId, "joao", UsuarioStatus.ATIVO));

        mockMvc.perform(get("/api/v1/auth/sessao")
                        .requestAttr(AuthController.ATRIBUTO_USUARIO_ID, usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario").value("joao"))
                .andExpect(jsonPath("$.status").value("ATIVO"));
    }
}
