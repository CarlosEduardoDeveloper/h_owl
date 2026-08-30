package com.example.foundation.modules.gamification.controller;

import com.example.foundation.modules.gamification.dto.RecompensaRequest;
import com.example.foundation.modules.gamification.dto.RecompensaResponse;
import com.example.foundation.modules.gamification.service.RecompensaService;
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
@RequestMapping("/api/v1/recompensas")
public class RecompensaController {

    private final RecompensaService service;

    public RecompensaController(RecompensaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RecompensaResponse> cadastrar(@RequestBody RecompensaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<RecompensaResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public RecompensaResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public RecompensaResponse atualizar(@PathVariable UUID id, @RequestBody RecompensaRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}