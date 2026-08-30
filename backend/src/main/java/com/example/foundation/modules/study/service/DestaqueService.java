package com.example.foundation.modules.study.service;

import com.example.foundation.modules.study.domain.Destaque;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.dto.DestaqueRequest;
import com.example.foundation.modules.study.dto.DestaqueResponse;
import com.example.foundation.modules.study.mapper.DestaqueMapper;
import com.example.foundation.modules.study.repository.DestaqueRepository;
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
public class DestaqueService {

    private final DestaqueRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final SessaoEstudoRepository sessaoEstudoRepository;

    public DestaqueService(
            DestaqueRepository repository,
            UsuarioRepository usuarioRepository,
            SessaoEstudoRepository sessaoEstudoRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.sessaoEstudoRepository = sessaoEstudoRepository;
    }

    @Transactional(readOnly = true)
    public List<DestaqueResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(DestaqueMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DestaqueResponse buscarAtivo(UUID id) {
        return DestaqueMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public DestaqueResponse criar(DestaqueRequest request) {
        Destaque entity = DestaqueMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return DestaqueMapper.toResponse(repository.save(entity));
    }

    public DestaqueResponse atualizar(UUID id, DestaqueRequest request) {
        Destaque entity = buscarEntidadeAtiva(id);
        DestaqueMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return DestaqueMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Destaque entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Destaque buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Destaque", id));
    }

    private void aplicarRelacionamentos(Destaque entity, DestaqueRequest request) {


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