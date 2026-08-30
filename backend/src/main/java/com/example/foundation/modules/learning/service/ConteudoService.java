package com.example.foundation.modules.learning.service;

import com.example.foundation.modules.learning.domain.Conteudo;
import com.example.foundation.modules.learning.domain.Modulo;
import com.example.foundation.modules.learning.dto.ConteudoRequest;
import com.example.foundation.modules.learning.dto.ConteudoResponse;
import com.example.foundation.modules.learning.mapper.ConteudoMapper;
import com.example.foundation.modules.learning.repository.ConteudoRepository;
import com.example.foundation.modules.learning.repository.ModuloRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConteudoService {

    private final ConteudoRepository repository;
    private final ModuloRepository moduloRepository;

    public ConteudoService(
            ConteudoRepository repository,
            ModuloRepository moduloRepository
    ) {
        this.repository = repository;
        this.moduloRepository = moduloRepository;
    }

    @Transactional(readOnly = true)
    public List<ConteudoResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(ConteudoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConteudoResponse buscarAtivo(UUID id) {
        return ConteudoMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public ConteudoResponse criar(ConteudoRequest request) {
        Conteudo entity = ConteudoMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return ConteudoMapper.toResponse(repository.save(entity));
    }

    public ConteudoResponse atualizar(UUID id, ConteudoRequest request) {
        Conteudo entity = buscarEntidadeAtiva(id);
        ConteudoMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return ConteudoMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Conteudo entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Conteudo buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conteudo", id));
    }

    private void aplicarRelacionamentos(Conteudo entity, ConteudoRequest request) {


        if (request.moduloId() != null) {
            Modulo modulo = moduloRepository.findByIdAndAtivoTrue(request.moduloId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Modulo", request.moduloId()));
            entity.setModulo(modulo);
        } else {
            entity.setModulo(null);
        }
    }
}