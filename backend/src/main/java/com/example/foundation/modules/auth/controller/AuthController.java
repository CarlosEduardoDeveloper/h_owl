package com.example.foundation.modules.auth.controller;

import com.example.foundation.modules.auth.dto.LoginRequest;
import com.example.foundation.modules.auth.dto.LoginResponse;
import com.example.foundation.modules.auth.dto.RegistrarRequest;
import com.example.foundation.modules.auth.dto.SessaoResponse;
import com.example.foundation.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    public static final String HEADER_USUARIO = "X-Usuario";
    public static final String HEADER_SENHA = "X-Senha";
    public static final String ATRIBUTO_USUARIO_ID = "usuarioId";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse registrar(@Valid @RequestBody RegistrarRequest request) {
        return authService.registrar(request);
    }

    @GetMapping("/sessao")
    public SessaoResponse sessao(@RequestAttribute(ATRIBUTO_USUARIO_ID) UUID usuarioId) {
        return authService.buscarSessao(usuarioId);
    }
}
