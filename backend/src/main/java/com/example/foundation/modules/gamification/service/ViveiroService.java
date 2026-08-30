package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.Viveiro;
import com.example.foundation.modules.gamification.dto.ViveiroRequest;
import com.example.foundation.modules.gamification.dto.ViveiroResponse;
import com.example.foundation.modules.gamification.mapper.ViveiroMapper;
import com.example.foundation.modules.gamification.repository.ViveiroRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ViveiroService {

    private final ViveiroRepository repository;
    private final UsuarioRepository usuarioRepository;

    public ViveiroService(
            ViveiroRepository repository,
            UsuarioRepository usuarioRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<ViveiroResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(ViveiroMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ViveiroResponse buscarAtivo(UUID id) {
        return ViveiroMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public ViveiroResponse criar(ViveiroRequest request) {
        Viveiro entity = ViveiroMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return ViveiroMapper.toResponse(repository.save(entity));
    }

    public ViveiroResponse atualizar(UUID id, ViveiroRequest request) {
        Viveiro entity = buscarEntidadeAtiva(id);
        ViveiroMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return ViveiroMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Viveiro entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Viveiro buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Viveiro", id));
    }

    private void aplicarRelacionamentos(Viveiro entity, ViveiroRequest request) {


        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(request.usuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
            entity.setUsuario(usuario);
        } else {
            entity.setUsuario(null);
        }
    }
}