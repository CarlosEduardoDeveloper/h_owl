package com.example.foundation.modules.study.service;

import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.dto.SessaoEstudoRequest;
import com.example.foundation.modules.study.dto.SessaoEstudoResponse;
import com.example.foundation.modules.study.mapper.SessaoEstudoMapper;
import com.example.foundation.modules.study.repository.SessaoEstudoRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SessaoEstudoService {

    private final SessaoEstudoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public SessaoEstudoService(
            SessaoEstudoRepository repository,
            UsuarioRepository usuarioRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<SessaoEstudoResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(SessaoEstudoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SessaoEstudoResponse buscarAtivo(UUID id) {
        return SessaoEstudoMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public SessaoEstudoResponse criar(SessaoEstudoRequest request) {
        SessaoEstudo entity = SessaoEstudoMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return SessaoEstudoMapper.toResponse(repository.save(entity));
    }

    public SessaoEstudoResponse atualizar(UUID id, SessaoEstudoRequest request) {
        SessaoEstudo entity = buscarEntidadeAtiva(id);
        SessaoEstudoMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return SessaoEstudoMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        SessaoEstudo entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private SessaoEstudo buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("SessaoEstudo", id));
    }

    private void aplicarRelacionamentos(SessaoEstudo entity, SessaoEstudoRequest request) {


        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(request.usuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
            entity.setUsuario(usuario);
        } else {
            entity.setUsuario(null);
        }
    }
}