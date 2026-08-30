package com.example.foundation.modules.sage.service;

import com.example.foundation.modules.sage.domain.ConsultaSabio;
import com.example.foundation.modules.sage.dto.ConsultaSabioRequest;
import com.example.foundation.modules.sage.dto.ConsultaSabioResponse;
import com.example.foundation.modules.sage.mapper.ConsultaSabioMapper;
import com.example.foundation.modules.sage.repository.ConsultaSabioRepository;
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
public class ConsultaSabioService {

    private final ConsultaSabioRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final SessaoEstudoRepository sessaoEstudoRepository;

    public ConsultaSabioService(
            ConsultaSabioRepository repository,
            UsuarioRepository usuarioRepository,
            SessaoEstudoRepository sessaoEstudoRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.sessaoEstudoRepository = sessaoEstudoRepository;
    }

    @Transactional(readOnly = true)
    public List<ConsultaSabioResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(ConsultaSabioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConsultaSabioResponse buscarAtivo(UUID id) {
        return ConsultaSabioMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public ConsultaSabioResponse criar(ConsultaSabioRequest request) {
        ConsultaSabio entity = ConsultaSabioMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return ConsultaSabioMapper.toResponse(repository.save(entity));
    }

    public ConsultaSabioResponse atualizar(UUID id, ConsultaSabioRequest request) {
        ConsultaSabio entity = buscarEntidadeAtiva(id);
        ConsultaSabioMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return ConsultaSabioMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        ConsultaSabio entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private ConsultaSabio buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("ConsultaSabio", id));
    }

    private void aplicarRelacionamentos(ConsultaSabio entity, ConsultaSabioRequest request) {


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