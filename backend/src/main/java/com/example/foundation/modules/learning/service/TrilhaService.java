package com.example.foundation.modules.learning.service;

import com.example.foundation.modules.learning.domain.Trilha;
import com.example.foundation.modules.learning.dto.TrilhaRequest;
import com.example.foundation.modules.learning.dto.TrilhaResponse;
import com.example.foundation.modules.learning.mapper.TrilhaMapper;
import com.example.foundation.modules.learning.repository.TrilhaRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrilhaService {

    private final TrilhaRepository repository;

    public TrilhaService(
            TrilhaRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TrilhaResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(TrilhaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TrilhaResponse buscarAtivo(UUID id) {
        return TrilhaMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public TrilhaResponse criar(TrilhaRequest request) {
        Trilha entity = TrilhaMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return TrilhaMapper.toResponse(repository.save(entity));
    }

    public TrilhaResponse atualizar(UUID id, TrilhaRequest request) {
        Trilha entity = buscarEntidadeAtiva(id);
        TrilhaMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return TrilhaMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Trilha entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Trilha buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Trilha", id));
    }

    private void aplicarRelacionamentos(Trilha entity, TrilhaRequest request) {
        // sem relacionamentos externos
    }
}