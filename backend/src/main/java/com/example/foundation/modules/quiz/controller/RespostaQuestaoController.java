package com.example.foundation.modules.quiz.controller;

import com.example.foundation.modules.quiz.dto.RespostaQuestaoRequest;
import com.example.foundation.modules.quiz.dto.RespostaQuestaoResponse;
import com.example.foundation.modules.quiz.service.RespostaQuestaoService;
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
@RequestMapping("/api/v1/respostas-questao")
public class RespostaQuestaoController {

    private final RespostaQuestaoService service;

    public RespostaQuestaoController(RespostaQuestaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RespostaQuestaoResponse> cadastrar(@RequestBody RespostaQuestaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<RespostaQuestaoResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public RespostaQuestaoResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public RespostaQuestaoResponse atualizar(@PathVariable UUID id, @RequestBody RespostaQuestaoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}