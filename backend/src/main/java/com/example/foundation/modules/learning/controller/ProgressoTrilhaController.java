package com.example.foundation.modules.learning.controller;

import com.example.foundation.modules.learning.dto.ProgressoTrilhaRequest;
import com.example.foundation.modules.learning.dto.ProgressoTrilhaResponse;
import com.example.foundation.modules.learning.service.ProgressoTrilhaService;
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
@RequestMapping("/api/v1/progressos-trilha")
public class ProgressoTrilhaController {

    private final ProgressoTrilhaService service;

    public ProgressoTrilhaController(ProgressoTrilhaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProgressoTrilhaResponse> cadastrar(@RequestBody ProgressoTrilhaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<ProgressoTrilhaResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public ProgressoTrilhaResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public ProgressoTrilhaResponse atualizar(@PathVariable UUID id, @RequestBody ProgressoTrilhaRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}