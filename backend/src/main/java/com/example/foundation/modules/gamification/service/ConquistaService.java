package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.Conquista;
import com.example.foundation.modules.gamification.dto.ConquistaRequest;
import com.example.foundation.modules.gamification.dto.ConquistaResponse;
import com.example.foundation.modules.gamification.mapper.ConquistaMapper;
import com.example.foundation.modules.gamification.repository.ConquistaRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConquistaService {

    private final ConquistaRepository repository;

    public ConquistaService(
            ConquistaRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ConquistaResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(ConquistaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConquistaResponse buscarAtivo(UUID id) {
        return ConquistaMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public ConquistaResponse criar(ConquistaRequest request) {
        Conquista entity = ConquistaMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return ConquistaMapper.toResponse(repository.save(entity));
    }

    public ConquistaResponse atualizar(UUID id, ConquistaRequest request) {
        Conquista entity = buscarEntidadeAtiva(id);
        ConquistaMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return ConquistaMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Conquista entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Conquista buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conquista", id));
    }

    private void aplicarRelacionamentos(Conquista entity, ConquistaRequest request) {
        // sem relacionamentos externos
    }
}