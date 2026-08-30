package com.example.foundation.modules.gamification.controller;

import com.example.foundation.modules.gamification.dto.CorujaUsuarioRequest;
import com.example.foundation.modules.gamification.dto.CorujaUsuarioResponse;
import com.example.foundation.modules.gamification.service.CorujaUsuarioService;
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
@RequestMapping("/api/v1/corujas-usuario")
public class CorujaUsuarioController {

    private final CorujaUsuarioService service;

    public CorujaUsuarioController(CorujaUsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CorujaUsuarioResponse> cadastrar(@RequestBody CorujaUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<CorujaUsuarioResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public CorujaUsuarioResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public CorujaUsuarioResponse atualizar(@PathVariable UUID id, @RequestBody CorujaUsuarioRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}