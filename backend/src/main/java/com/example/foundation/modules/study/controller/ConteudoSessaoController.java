package com.example.foundation.modules.study.controller;

import com.example.foundation.modules.study.dto.ConteudoSessaoRequest;
import com.example.foundation.modules.study.dto.ConteudoSessaoResponse;
import com.example.foundation.modules.study.service.ConteudoSessaoService;
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
@RequestMapping("/api/v1/conteudos-sessao")
public class ConteudoSessaoController {

    private final ConteudoSessaoService service;

    public ConteudoSessaoController(ConteudoSessaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ConteudoSessaoResponse> cadastrar(@RequestBody ConteudoSessaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<ConteudoSessaoResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public ConteudoSessaoResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public ConteudoSessaoResponse atualizar(@PathVariable UUID id, @RequestBody ConteudoSessaoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}