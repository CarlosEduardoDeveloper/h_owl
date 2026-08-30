package com.example.foundation.modules.sage.controller;

import java.util.UUID;

import com.example.foundation.modules.auth.controller.AuthController;
import com.example.foundation.modules.sage.dto.ConsultaSabioResponse;
import com.example.foundation.modules.sage.dto.PerguntarSabioRequest;
import com.example.foundation.modules.sage.service.SabioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sabio")
public class SabioController {

    private final SabioService sabioService;

    public SabioController(SabioService sabioService) {
        this.sabioService = sabioService;
    }

    @PostMapping("/consultas")
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaSabioResponse perguntar(
            @RequestAttribute(AuthController.ATRIBUTO_USUARIO_ID) UUID usuarioId,
            @Valid @RequestBody PerguntarSabioRequest request
    ) {
        return sabioService.perguntar(usuarioId, request);
    }
}
