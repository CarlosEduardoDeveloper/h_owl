package com.example.foundation.modules.user.service;

import com.example.foundation.modules.user.domain.Pessoa;
import com.example.foundation.modules.user.dto.PessoaRequest;
import com.example.foundation.modules.user.dto.PessoaResponse;
import com.example.foundation.modules.user.mapper.PessoaMapper;
import com.example.foundation.modules.user.repository.PessoaRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PessoaService {

    private final PessoaRepository repository;

    public PessoaService(
            PessoaRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<PessoaResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(PessoaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PessoaResponse buscarAtivo(UUID id) {
        return PessoaMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public PessoaResponse criar(PessoaRequest request) {
        Pessoa entity = PessoaMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return PessoaMapper.toResponse(repository.save(entity));
    }

    public PessoaResponse atualizar(UUID id, PessoaRequest request) {
        Pessoa entity = buscarEntidadeAtiva(id);
        PessoaMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return PessoaMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Pessoa entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Pessoa buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa", id));
    }

    private void aplicarRelacionamentos(Pessoa entity, PessoaRequest request) {
        // sem relacionamentos externos
    }
}