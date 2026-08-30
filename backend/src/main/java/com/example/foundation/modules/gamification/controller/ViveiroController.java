package com.example.foundation.modules.gamification.controller;

import com.example.foundation.modules.gamification.dto.ViveiroRequest;
import com.example.foundation.modules.gamification.dto.ViveiroResponse;
import com.example.foundation.modules.gamification.service.ViveiroService;
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
@RequestMapping("/api/v1/viveiros")
public class ViveiroController {

    private final ViveiroService service;

    public ViveiroController(ViveiroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ViveiroResponse> cadastrar(@RequestBody ViveiroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<ViveiroResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public ViveiroResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public ViveiroResponse atualizar(@PathVariable UUID id, @RequestBody ViveiroRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}