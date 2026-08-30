package com.example.foundation.modules.user.controller;

import com.example.foundation.modules.user.dto.PreferenciaUsuarioRequest;
import com.example.foundation.modules.user.dto.PreferenciaUsuarioResponse;
import com.example.foundation.modules.user.service.PreferenciaUsuarioService;
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
@RequestMapping("/api/v1/preferencias-usuario")
public class PreferenciaUsuarioController {

    private final PreferenciaUsuarioService service;

    public PreferenciaUsuarioController(PreferenciaUsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PreferenciaUsuarioResponse> cadastrar(@RequestBody PreferenciaUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<PreferenciaUsuarioResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public PreferenciaUsuarioResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public PreferenciaUsuarioResponse atualizar(@PathVariable UUID id, @RequestBody PreferenciaUsuarioRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}