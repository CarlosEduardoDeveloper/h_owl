package com.example.foundation.modules.gamification.controller;

import com.example.foundation.modules.gamification.dto.TipoOvoRequest;
import com.example.foundation.modules.gamification.dto.TipoOvoResponse;
import com.example.foundation.modules.gamification.service.TipoOvoService;
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
@RequestMapping("/api/v1/tipos-ovo")
public class TipoOvoController {

    private final TipoOvoService service;

    public TipoOvoController(TipoOvoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TipoOvoResponse> cadastrar(@RequestBody TipoOvoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<TipoOvoResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public TipoOvoResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public TipoOvoResponse atualizar(@PathVariable UUID id, @RequestBody TipoOvoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}