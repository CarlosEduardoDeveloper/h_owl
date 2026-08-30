package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.Coruja;
import com.example.foundation.modules.gamification.domain.OvoUsuario;
import com.example.foundation.modules.gamification.domain.Recompensa;
import com.example.foundation.modules.gamification.dto.RecompensaRequest;
import com.example.foundation.modules.gamification.dto.RecompensaResponse;
import com.example.foundation.modules.gamification.mapper.RecompensaMapper;
import com.example.foundation.modules.gamification.repository.CorujaRepository;
import com.example.foundation.modules.gamification.repository.OvoUsuarioRepository;
import com.example.foundation.modules.gamification.repository.RecompensaRepository;
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
public class RecompensaService {

    private final RecompensaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final CorujaRepository corujaRepository;
    private final SessaoEstudoRepository sessaoEstudoRepository;
    private final OvoUsuarioRepository ovoUsuarioRepository;

    public RecompensaService(
            RecompensaRepository repository,
            UsuarioRepository usuarioRepository,
            CorujaRepository corujaRepository,
            SessaoEstudoRepository sessaoEstudoRepository,
            OvoUsuarioRepository ovoUsuarioRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.corujaRepository = corujaRepository;
        this.sessaoEstudoRepository = sessaoEstudoRepository;
        this.ovoUsuarioRepository = ovoUsuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<RecompensaResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(RecompensaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RecompensaResponse buscarAtivo(UUID id) {
        return RecompensaMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public RecompensaResponse criar(RecompensaRequest request) {
        Recompensa entity = RecompensaMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return RecompensaMapper.toResponse(repository.save(entity));
    }

    public RecompensaResponse atualizar(UUID id, RecompensaRequest request) {
        Recompensa entity = buscarEntidadeAtiva(id);
        RecompensaMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return RecompensaMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Recompensa entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Recompensa buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Recompensa", id));
    }

    private void aplicarRelacionamentos(Recompensa entity, RecompensaRequest request) {


        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(request.usuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
            entity.setUsuario(usuario);
        } else {
            entity.setUsuario(null);
        }

        if (request.corujaId() != null) {
            Coruja coruja = corujaRepository.findByIdAndAtivoTrue(request.corujaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Coruja", request.corujaId()));
            entity.setCoruja(coruja);
        } else {
            entity.setCoruja(null);
        }

        if (request.sessaoEstudoId() != null) {
            SessaoEstudo sessaoEstudo = sessaoEstudoRepository.findByIdAndAtivoTrue(request.sessaoEstudoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("SessaoEstudo", request.sessaoEstudoId()));
            entity.setSessaoEstudo(sessaoEstudo);
        } else {
            entity.setSessaoEstudo(null);
        }

        if (request.ovoUsuarioId() != null) {
            OvoUsuario ovoUsuario = ovoUsuarioRepository.findByIdAndAtivoTrue(request.ovoUsuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("OvoUsuario", request.ovoUsuarioId()));
            entity.setOvoUsuario(ovoUsuario);
        } else {
            entity.setOvoUsuario(null);
        }
    }
}