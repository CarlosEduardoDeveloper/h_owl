package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.Conquista;
import com.example.foundation.modules.gamification.domain.ConquistaUsuario;
import com.example.foundation.modules.gamification.dto.ConquistaUsuarioRequest;
import com.example.foundation.modules.gamification.dto.ConquistaUsuarioResponse;
import com.example.foundation.modules.gamification.mapper.ConquistaUsuarioMapper;
import com.example.foundation.modules.gamification.repository.ConquistaRepository;
import com.example.foundation.modules.gamification.repository.ConquistaUsuarioRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConquistaUsuarioService {

    private final ConquistaUsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ConquistaRepository conquistaRepository;

    public ConquistaUsuarioService(
            ConquistaUsuarioRepository repository,
            UsuarioRepository usuarioRepository,
            ConquistaRepository conquistaRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.conquistaRepository = conquistaRepository;
    }

    @Transactional(readOnly = true)
    public List<ConquistaUsuarioResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(ConquistaUsuarioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConquistaUsuarioResponse buscarAtivo(UUID id) {
        return ConquistaUsuarioMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public ConquistaUsuarioResponse criar(ConquistaUsuarioRequest request) {
        ConquistaUsuario entity = ConquistaUsuarioMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return ConquistaUsuarioMapper.toResponse(repository.save(entity));
    }

    public ConquistaUsuarioResponse atualizar(UUID id, ConquistaUsuarioRequest request) {
        ConquistaUsuario entity = buscarEntidadeAtiva(id);
        ConquistaUsuarioMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return ConquistaUsuarioMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        ConquistaUsuario entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private ConquistaUsuario buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("ConquistaUsuario", id));
    }

    private void aplicarRelacionamentos(ConquistaUsuario entity, ConquistaUsuarioRequest request) {


        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(request.usuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
            entity.setUsuario(usuario);
        } else {
            entity.setUsuario(null);
        }

        if (request.conquistaId() != null) {
            Conquista conquista = conquistaRepository.findByIdAndAtivoTrue(request.conquistaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Conquista", request.conquistaId()));
            entity.setConquista(conquista);
        } else {
            entity.setConquista(null);
        }
    }
}