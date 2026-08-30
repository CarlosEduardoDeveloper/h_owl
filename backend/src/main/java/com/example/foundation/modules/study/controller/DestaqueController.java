package com.example.foundation.modules.study.controller;

import com.example.foundation.modules.study.dto.DestaqueRequest;
import com.example.foundation.modules.study.dto.DestaqueResponse;
import com.example.foundation.modules.study.service.DestaqueService;
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
@RequestMapping("/api/v1/destaques")
public class DestaqueController {

    private final DestaqueService service;

    public DestaqueController(DestaqueService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DestaqueResponse> cadastrar(@RequestBody DestaqueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<DestaqueResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public DestaqueResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public DestaqueResponse atualizar(@PathVariable UUID id, @RequestBody DestaqueRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}