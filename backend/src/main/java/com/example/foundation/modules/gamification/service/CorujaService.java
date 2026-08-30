package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.Coruja;
import com.example.foundation.modules.gamification.dto.CorujaRequest;
import com.example.foundation.modules.gamification.dto.CorujaResponse;
import com.example.foundation.modules.gamification.mapper.CorujaMapper;
import com.example.foundation.modules.gamification.repository.CorujaRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CorujaService {

    private final CorujaRepository repository;

    public CorujaService(
            CorujaRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CorujaResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(CorujaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CorujaResponse buscarAtivo(UUID id) {
        return CorujaMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public CorujaResponse criar(CorujaRequest request) {
        Coruja entity = CorujaMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return CorujaMapper.toResponse(repository.save(entity));
    }

    public CorujaResponse atualizar(UUID id, CorujaRequest request) {
        Coruja entity = buscarEntidadeAtiva(id);
        CorujaMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return CorujaMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Coruja entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Coruja buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Coruja", id));
    }

    private void aplicarRelacionamentos(Coruja entity, CorujaRequest request) {
        // sem relacionamentos externos
    }
}