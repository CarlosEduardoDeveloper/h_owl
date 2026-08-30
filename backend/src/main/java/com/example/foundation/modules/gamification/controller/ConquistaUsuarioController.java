package com.example.foundation.modules.gamification.controller;

import com.example.foundation.modules.gamification.dto.ConquistaUsuarioRequest;
import com.example.foundation.modules.gamification.dto.ConquistaUsuarioResponse;
import com.example.foundation.modules.gamification.service.ConquistaUsuarioService;
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
@RequestMapping("/api/v1/conquistas-usuario")
public class ConquistaUsuarioController {

    private final ConquistaUsuarioService service;

    public ConquistaUsuarioController(ConquistaUsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ConquistaUsuarioResponse> cadastrar(@RequestBody ConquistaUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<ConquistaUsuarioResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public ConquistaUsuarioResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public ConquistaUsuarioResponse atualizar(@PathVariable UUID id, @RequestBody ConquistaUsuarioRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}