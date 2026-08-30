package com.example.foundation.modules.gamification.controller;

import com.example.foundation.modules.gamification.dto.CorujaRequest;
import com.example.foundation.modules.gamification.dto.CorujaResponse;
import com.example.foundation.modules.gamification.service.CorujaService;
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
@RequestMapping("/api/v1/corujas")
public class CorujaController {

    private final CorujaService service;

    public CorujaController(CorujaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CorujaResponse> cadastrar(@RequestBody CorujaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<CorujaResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public CorujaResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public CorujaResponse atualizar(@PathVariable UUID id, @RequestBody CorujaRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}