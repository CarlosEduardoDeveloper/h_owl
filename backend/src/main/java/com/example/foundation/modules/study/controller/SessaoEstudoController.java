package com.example.foundation.modules.study.controller;

import com.example.foundation.modules.study.dto.SessaoEstudoConcluirRequest;
import com.example.foundation.modules.study.dto.SessaoEstudoRequest;
import com.example.foundation.modules.study.dto.SessaoEstudoResponse;
import com.example.foundation.modules.study.service.SessaoEstudoService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sessoes-estudo")
public class SessaoEstudoController {

    private final SessaoEstudoService service;

    public SessaoEstudoController(SessaoEstudoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SessaoEstudoResponse> cadastrar(@RequestBody SessaoEstudoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<SessaoEstudoResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public SessaoEstudoResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public SessaoEstudoResponse atualizar(@PathVariable UUID id, @RequestBody SessaoEstudoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/iniciar")
    public SessaoEstudoResponse iniciar(@PathVariable UUID id) {
        return service.iniciar(id);
    }

    @PostMapping("/{id}/concluir")
    public SessaoEstudoResponse concluir(
            @PathVariable UUID id,
            @RequestBody(required = false) SessaoEstudoConcluirRequest request
    ) {
        return service.concluir(id, request);
    }

    @PostMapping("/{id}/interromper")
    public SessaoEstudoResponse interromper(@PathVariable UUID id) {
        return service.interromper(id);
    }
}