package com.example.foundation.modules.study.service;

import com.example.foundation.modules.gamification.dto.GamificacaoSessaoResponse;
import com.example.foundation.modules.gamification.service.EstudoGamificacaoService;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.domain.enums.SessaoEstudoStatus;
import com.example.foundation.modules.study.dto.SessaoEstudoConclusaoResponse;
import com.example.foundation.modules.study.dto.SessaoEstudoConcluirRequest;
import com.example.foundation.modules.study.dto.SessaoEstudoRequest;
import com.example.foundation.modules.study.dto.SessaoEstudoResponse;
import com.example.foundation.modules.study.mapper.SessaoEstudoMapper;
import com.example.foundation.modules.study.repository.SessaoEstudoRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.OperacaoInvalidaException;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SessaoEstudoService {

    private final SessaoEstudoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EstudoGamificacaoService estudoGamificacaoService;

    public SessaoEstudoService(
            SessaoEstudoRepository repository,
            UsuarioRepository usuarioRepository,
            EstudoGamificacaoService estudoGamificacaoService
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.estudoGamificacaoService = estudoGamificacaoService;
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

    public SessaoEstudoResponse iniciar(UUID id) {
        SessaoEstudo entity = buscarEntidadeAtiva(id);
        if (entity.getStatus() != null && entity.getStatus() != SessaoEstudoStatus.CRIADA) {
            throw new OperacaoInvalidaException("Sessão só pode ser iniciada a partir do status CRIADA");
        }
        entity.setStatus(SessaoEstudoStatus.EM_ANDAMENTO);
        entity.setInicioEm(Instant.now());
        SessaoEstudo salva = repository.save(entity);
        estudoGamificacaoService.aoIniciarSessao(salva);
        return SessaoEstudoMapper.toResponse(salva);
    }

    public SessaoEstudoConclusaoResponse concluir(UUID id, SessaoEstudoConcluirRequest request) {
        SessaoEstudo entity = buscarEntidadeAtiva(id);
        if (entity.getStatus() != SessaoEstudoStatus.EM_ANDAMENTO) {
            throw new OperacaoInvalidaException("Sessão só pode ser concluída quando estiver EM_ANDAMENTO");
        }
        entity.setStatus(SessaoEstudoStatus.CONCLUIDA);
        entity.setFimEm(Instant.now());
        if (request != null && request.duracaoRealMinutos() != null) {
            entity.setDuracaoRealMinutos(request.duracaoRealMinutos());
        } else if (entity.getDuracaoPlanejadaMinutos() != null) {
            entity.setDuracaoRealMinutos(entity.getDuracaoPlanejadaMinutos());
        }
        SessaoEstudo salva = repository.save(entity);
        GamificacaoSessaoResponse gamificacao = estudoGamificacaoService.aoConcluirSessao(salva);
        return SessaoEstudoConclusaoResponse.from(SessaoEstudoMapper.toResponse(salva), gamificacao);
    }

    public SessaoEstudoResponse interromper(UUID id) {
        SessaoEstudo entity = buscarEntidadeAtiva(id);
        if (entity.getStatus() != SessaoEstudoStatus.EM_ANDAMENTO) {
            throw new OperacaoInvalidaException("Sessão só pode ser interrompida quando estiver EM_ANDAMENTO");
        }
        entity.setStatus(SessaoEstudoStatus.INTERROMPIDA);
        entity.setFimEm(Instant.now());
        SessaoEstudo salva = repository.save(entity);
        estudoGamificacaoService.aoInterromperSessao(salva);
        return SessaoEstudoMapper.toResponse(salva);
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