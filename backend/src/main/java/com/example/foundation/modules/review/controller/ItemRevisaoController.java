package com.example.foundation.modules.review.controller;

import com.example.foundation.modules.review.dto.ItemRevisaoRequest;
import com.example.foundation.modules.review.dto.ItemRevisaoResponse;
import com.example.foundation.modules.review.service.ItemRevisaoService;
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
@RequestMapping("/api/v1/itens-revisao")
public class ItemRevisaoController {

    private final ItemRevisaoService service;

    public ItemRevisaoController(ItemRevisaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ItemRevisaoResponse> cadastrar(@RequestBody ItemRevisaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<ItemRevisaoResponse> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public ItemRevisaoResponse buscar(@PathVariable UUID id) {
        return service.buscarAtivo(id);
    }

    @PutMapping("/{id}")
    public ItemRevisaoResponse atualizar(@PathVariable UUID id, @RequestBody ItemRevisaoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}