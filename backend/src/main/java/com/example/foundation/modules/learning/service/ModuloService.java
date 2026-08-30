package com.example.foundation.modules.learning.service;

import com.example.foundation.modules.learning.domain.Modulo;
import com.example.foundation.modules.learning.domain.Trilha;
import com.example.foundation.modules.learning.dto.ModuloRequest;
import com.example.foundation.modules.learning.dto.ModuloResponse;
import com.example.foundation.modules.learning.mapper.ModuloMapper;
import com.example.foundation.modules.learning.repository.ModuloRepository;
import com.example.foundation.modules.learning.repository.TrilhaRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModuloService {

    private final ModuloRepository repository;
    private final TrilhaRepository trilhaRepository;

    public ModuloService(
            ModuloRepository repository,
            TrilhaRepository trilhaRepository
    ) {
        this.repository = repository;
        this.trilhaRepository = trilhaRepository;
    }

    @Transactional(readOnly = true)
    public List<ModuloResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(ModuloMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ModuloResponse buscarAtivo(UUID id) {
        return ModuloMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public ModuloResponse criar(ModuloRequest request) {
        Modulo entity = ModuloMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return ModuloMapper.toResponse(repository.save(entity));
    }

    public ModuloResponse atualizar(UUID id, ModuloRequest request) {
        Modulo entity = buscarEntidadeAtiva(id);
        ModuloMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return ModuloMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Modulo entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Modulo buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Modulo", id));
    }

    private void aplicarRelacionamentos(Modulo entity, ModuloRequest request) {


        if (request.trilhaId() != null) {
            Trilha trilha = trilhaRepository.findByIdAndAtivoTrue(request.trilhaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Trilha", request.trilhaId()));
            entity.setTrilha(trilha);
        } else {
            entity.setTrilha(null);
        }
    }
}