package com.example.foundation.modules.quiz.controller;

import com.example.foundation.modules.quiz.dto.AlternativaRequest;
import com.example.foundation.modules.quiz.dto.AlternativaResponse;
import com.example.foundation.modules.quiz.service.AlternativaService;
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
@RequestMapping("/api/v1/alternativas")
public class AlternativaController {

    private final AlternativaService service;

    public AlternativaController(AlternativaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AlternativaResponse> cadastrar(@RequestBody AlternativaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<AlternativaResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public AlternativaResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public AlternativaResponse atualizar(@PathVariable UUID id, @RequestBody AlternativaRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}