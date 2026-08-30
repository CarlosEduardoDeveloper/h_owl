package com.example.foundation.modules.user.controller;

import java.util.List;
import java.util.UUID;

import com.example.foundation.modules.auth.controller.AuthController;
import com.example.foundation.modules.sage.dto.ConsultaSabioResponse;
import com.example.foundation.modules.user.dto.MeResumoResponse;
import com.example.foundation.modules.user.service.MeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    private final MeService meService;

    public MeController(MeService meService) {
        this.meService = meService;
    }

    @GetMapping("/resumo")
    public MeResumoResponse resumo(@RequestAttribute(AuthController.ATRIBUTO_USUARIO_ID) UUID usuarioId) {
        return meService.buscarResumo(usuarioId);
    }

    @GetMapping("/consultas-sabio")
    public List<ConsultaSabioResponse> consultasSabio(
            @RequestAttribute(AuthController.ATRIBUTO_USUARIO_ID) UUID usuarioId
    ) {
        return meService.listarConsultasSabio(usuarioId);
    }
}
