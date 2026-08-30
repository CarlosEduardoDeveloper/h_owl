package com.example.foundation.modules.gamification.controller;

import com.example.foundation.modules.gamification.dto.DecoracaoViveiroRequest;
import com.example.foundation.modules.gamification.dto.DecoracaoViveiroResponse;
import com.example.foundation.modules.gamification.service.DecoracaoViveiroService;
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
@RequestMapping("/api/v1/decoracoes-viveiro")
public class DecoracaoViveiroController {

    private final DecoracaoViveiroService service;

    public DecoracaoViveiroController(DecoracaoViveiroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DecoracaoViveiroResponse> cadastrar(@RequestBody DecoracaoViveiroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<DecoracaoViveiroResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public DecoracaoViveiroResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public DecoracaoViveiroResponse atualizar(@PathVariable UUID id, @RequestBody DecoracaoViveiroRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}