package com.example.foundation.modules.quiz.controller;

import com.example.foundation.modules.quiz.dto.TentativaQuizRequest;
import com.example.foundation.modules.quiz.dto.TentativaQuizResponse;
import com.example.foundation.modules.quiz.service.TentativaQuizService;
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
@RequestMapping("/api/v1/tentativas-quiz")
public class TentativaQuizController {

    private final TentativaQuizService service;

    public TentativaQuizController(TentativaQuizService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TentativaQuizResponse> cadastrar(@RequestBody TentativaQuizRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<TentativaQuizResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public TentativaQuizResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public TentativaQuizResponse atualizar(@PathVariable UUID id, @RequestBody TentativaQuizRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}