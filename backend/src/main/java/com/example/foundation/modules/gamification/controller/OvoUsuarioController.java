package com.example.foundation.modules.gamification.controller;

import com.example.foundation.modules.gamification.dto.OvoUsuarioRequest;
import com.example.foundation.modules.gamification.dto.OvoUsuarioResponse;
import com.example.foundation.modules.gamification.service.OvoUsuarioService;
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
@RequestMapping("/api/v1/ovos-usuario")
public class OvoUsuarioController {

    private final OvoUsuarioService service;

    public OvoUsuarioController(OvoUsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OvoUsuarioResponse> cadastrar(@RequestBody OvoUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<OvoUsuarioResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public OvoUsuarioResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public OvoUsuarioResponse atualizar(@PathVariable UUID id, @RequestBody OvoUsuarioRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}