package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.OvoUsuario;
import com.example.foundation.modules.gamification.domain.TipoOvo;
import com.example.foundation.modules.gamification.dto.OvoUsuarioRequest;
import com.example.foundation.modules.gamification.dto.OvoUsuarioResponse;
import com.example.foundation.modules.gamification.mapper.OvoUsuarioMapper;
import com.example.foundation.modules.gamification.repository.OvoUsuarioRepository;
import com.example.foundation.modules.gamification.repository.TipoOvoRepository;
import com.example.foundation.modules.study.domain.SessaoEstudo;
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
public class OvoUsuarioService {

    private final OvoUsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final TipoOvoRepository tipoOvoRepository;
    private final SessaoEstudoRepository sessaoEstudoRepository;

    public OvoUsuarioService(
            OvoUsuarioRepository repository,
            UsuarioRepository usuarioRepository,
            TipoOvoRepository tipoOvoRepository,
            SessaoEstudoRepository sessaoEstudoRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.tipoOvoRepository = tipoOvoRepository;
        this.sessaoEstudoRepository = sessaoEstudoRepository;
    }

    @Transactional(readOnly = true)
    public List<OvoUsuarioResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(OvoUsuarioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OvoUsuarioResponse buscarAtivo(UUID id) {
        return OvoUsuarioMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public OvoUsuarioResponse criar(OvoUsuarioRequest request) {
        OvoUsuario entity = OvoUsuarioMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return OvoUsuarioMapper.toResponse(repository.save(entity));
    }

    public OvoUsuarioResponse atualizar(UUID id, OvoUsuarioRequest request) {
        OvoUsuario entity = buscarEntidadeAtiva(id);
        OvoUsuarioMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return OvoUsuarioMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        OvoUsuario entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private OvoUsuario buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("OvoUsuario", id));
    }

    private void aplicarRelacionamentos(OvoUsuario entity, OvoUsuarioRequest request) {


        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(request.usuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
            entity.setUsuario(usuario);
        } else {
            entity.setUsuario(null);
        }

        if (request.tipoOvoId() != null) {
            TipoOvo tipoOvo = tipoOvoRepository.findByIdAndAtivoTrue(request.tipoOvoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("TipoOvo", request.tipoOvoId()));
            entity.setTipoOvo(tipoOvo);
        } else {
            entity.setTipoOvo(null);
        }

        if (request.sessaoEstudoId() != null) {
            SessaoEstudo sessaoEstudo = sessaoEstudoRepository.findByIdAndAtivoTrue(request.sessaoEstudoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("SessaoEstudo", request.sessaoEstudoId()));
            entity.setSessaoEstudo(sessaoEstudo);
        } else {
            entity.setSessaoEstudo(null);
        }
    }
}