package com.example.foundation.modules.learning.service;

import com.example.foundation.modules.learning.domain.Modulo;
import com.example.foundation.modules.learning.domain.ProgressoTrilha;
import com.example.foundation.modules.learning.domain.Trilha;
import com.example.foundation.modules.learning.dto.ProgressoTrilhaRequest;
import com.example.foundation.modules.learning.dto.ProgressoTrilhaResponse;
import com.example.foundation.modules.learning.mapper.ProgressoTrilhaMapper;
import com.example.foundation.modules.learning.repository.ModuloRepository;
import com.example.foundation.modules.learning.repository.ProgressoTrilhaRepository;
import com.example.foundation.modules.learning.repository.TrilhaRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProgressoTrilhaService {

    private final ProgressoTrilhaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final TrilhaRepository trilhaRepository;
    private final ModuloRepository moduloRepository;

    public ProgressoTrilhaService(
            ProgressoTrilhaRepository repository,
            UsuarioRepository usuarioRepository,
            TrilhaRepository trilhaRepository,
            ModuloRepository moduloRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.trilhaRepository = trilhaRepository;
        this.moduloRepository = moduloRepository;
    }

    @Transactional(readOnly = true)
    public List<ProgressoTrilhaResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(ProgressoTrilhaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProgressoTrilhaResponse buscarAtivo(UUID id) {
        return ProgressoTrilhaMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public ProgressoTrilhaResponse criar(ProgressoTrilhaRequest request) {
        ProgressoTrilha entity = ProgressoTrilhaMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return ProgressoTrilhaMapper.toResponse(repository.save(entity));
    }

    public ProgressoTrilhaResponse atualizar(UUID id, ProgressoTrilhaRequest request) {
        ProgressoTrilha entity = buscarEntidadeAtiva(id);
        ProgressoTrilhaMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return ProgressoTrilhaMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        ProgressoTrilha entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private ProgressoTrilha buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("ProgressoTrilha", id));
    }

    private void aplicarRelacionamentos(ProgressoTrilha entity, ProgressoTrilhaRequest request) {


        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(request.usuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
            entity.setUsuario(usuario);
        } else {
            entity.setUsuario(null);
        }

        if (request.trilhaId() != null) {
            Trilha trilha = trilhaRepository.findByIdAndAtivoTrue(request.trilhaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Trilha", request.trilhaId()));
            entity.setTrilha(trilha);
        } else {
            entity.setTrilha(null);
        }

        if (request.moduloAtualId() != null) {
            Modulo moduloAtual = moduloRepository.findByIdAndAtivoTrue(request.moduloAtualId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Modulo", request.moduloAtualId()));
            entity.setModuloAtual(moduloAtual);
        } else {
            entity.setModuloAtual(null);
        }
    }
}