package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.TipoOvo;
import com.example.foundation.modules.gamification.dto.TipoOvoRequest;
import com.example.foundation.modules.gamification.dto.TipoOvoResponse;
import com.example.foundation.modules.gamification.mapper.TipoOvoMapper;
import com.example.foundation.modules.gamification.repository.TipoOvoRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TipoOvoService {

    private final TipoOvoRepository repository;

    public TipoOvoService(
            TipoOvoRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TipoOvoResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(TipoOvoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TipoOvoResponse buscarAtivo(UUID id) {
        return TipoOvoMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public TipoOvoResponse criar(TipoOvoRequest request) {
        TipoOvo entity = TipoOvoMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return TipoOvoMapper.toResponse(repository.save(entity));
    }

    public TipoOvoResponse atualizar(UUID id, TipoOvoRequest request) {
        TipoOvo entity = buscarEntidadeAtiva(id);
        TipoOvoMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return TipoOvoMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        TipoOvo entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private TipoOvo buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("TipoOvo", id));
    }

    private void aplicarRelacionamentos(TipoOvo entity, TipoOvoRequest request) {
        // sem relacionamentos externos
    }
}