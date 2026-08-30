package com.example.foundation.modules.learning.controller;

import com.example.foundation.modules.learning.dto.ConteudoRequest;
import com.example.foundation.modules.learning.dto.ConteudoResponse;
import com.example.foundation.modules.learning.service.ConteudoService;
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
@RequestMapping("/api/v1/conteudos")
public class ConteudoController {

    private final ConteudoService service;

    public ConteudoController(ConteudoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ConteudoResponse> cadastrar(@RequestBody ConteudoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<ConteudoResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public ConteudoResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public ConteudoResponse atualizar(@PathVariable UUID id, @RequestBody ConteudoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}