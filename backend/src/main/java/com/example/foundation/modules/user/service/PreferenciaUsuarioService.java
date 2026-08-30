package com.example.foundation.modules.user.service;

import com.example.foundation.modules.user.domain.PreferenciaUsuario;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.dto.PreferenciaUsuarioRequest;
import com.example.foundation.modules.user.dto.PreferenciaUsuarioResponse;
import com.example.foundation.modules.user.mapper.PreferenciaUsuarioMapper;
import com.example.foundation.modules.user.repository.PreferenciaUsuarioRepository;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PreferenciaUsuarioService {

    private final PreferenciaUsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;

    public PreferenciaUsuarioService(
            PreferenciaUsuarioRepository repository,
            UsuarioRepository usuarioRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<PreferenciaUsuarioResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(PreferenciaUsuarioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PreferenciaUsuarioResponse buscarAtivo(UUID id) {
        return PreferenciaUsuarioMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public PreferenciaUsuarioResponse criar(PreferenciaUsuarioRequest request) {
        PreferenciaUsuario entity = PreferenciaUsuarioMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return PreferenciaUsuarioMapper.toResponse(repository.save(entity));
    }

    public PreferenciaUsuarioResponse atualizar(UUID id, PreferenciaUsuarioRequest request) {
        PreferenciaUsuario entity = buscarEntidadeAtiva(id);
        PreferenciaUsuarioMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return PreferenciaUsuarioMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        PreferenciaUsuario entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private PreferenciaUsuario buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("PreferenciaUsuario", id));
    }

    private void aplicarRelacionamentos(PreferenciaUsuario entity, PreferenciaUsuarioRequest request) {


        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(request.usuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
            entity.setUsuario(usuario);
        } else {
            entity.setUsuario(null);
        }
    }
}