package com.example.foundation.modules.study.service;

import com.example.foundation.modules.study.domain.Nota;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.dto.NotaRequest;
import com.example.foundation.modules.study.dto.NotaResponse;
import com.example.foundation.modules.study.mapper.NotaMapper;
import com.example.foundation.modules.study.repository.NotaRepository;
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
public class NotaService {

    private final NotaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final SessaoEstudoRepository sessaoEstudoRepository;

    public NotaService(
            NotaRepository repository,
            UsuarioRepository usuarioRepository,
            SessaoEstudoRepository sessaoEstudoRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.sessaoEstudoRepository = sessaoEstudoRepository;
    }

    @Transactional(readOnly = true)
    public List<NotaResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(NotaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public NotaResponse buscarAtivo(UUID id) {
        return NotaMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public NotaResponse criar(NotaRequest request) {
        Nota entity = NotaMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return NotaMapper.toResponse(repository.save(entity));
    }

    public NotaResponse atualizar(UUID id, NotaRequest request) {
        Nota entity = buscarEntidadeAtiva(id);
        NotaMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return NotaMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Nota entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Nota buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nota", id));
    }

    private void aplicarRelacionamentos(Nota entity, NotaRequest request) {


        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(request.usuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
            entity.setUsuario(usuario);
        } else {
            entity.setUsuario(null);
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