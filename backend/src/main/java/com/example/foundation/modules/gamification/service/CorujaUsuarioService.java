package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.Coruja;
import com.example.foundation.modules.gamification.domain.CorujaUsuario;
import com.example.foundation.modules.gamification.dto.CorujaUsuarioRequest;
import com.example.foundation.modules.gamification.dto.CorujaUsuarioResponse;
import com.example.foundation.modules.gamification.mapper.CorujaUsuarioMapper;
import com.example.foundation.modules.gamification.repository.CorujaRepository;
import com.example.foundation.modules.gamification.repository.CorujaUsuarioRepository;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.repository.SessaoEstudoRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.OperacaoInvalidaException;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CorujaUsuarioService {

    private final CorujaUsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final CorujaRepository corujaRepository;
    private final SessaoEstudoRepository sessaoEstudoRepository;
    private final BiscoitoService biscoitoService;

    public CorujaUsuarioService(
            CorujaUsuarioRepository repository,
            UsuarioRepository usuarioRepository,
            CorujaRepository corujaRepository,
            SessaoEstudoRepository sessaoEstudoRepository,
            BiscoitoService biscoitoService
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.corujaRepository = corujaRepository;
        this.sessaoEstudoRepository = sessaoEstudoRepository;
        this.biscoitoService = biscoitoService;
    }

    @Transactional(readOnly = true)
    public List<CorujaUsuarioResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(CorujaUsuarioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CorujaUsuarioResponse buscarAtivo(UUID id) {
        return CorujaUsuarioMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public CorujaUsuarioResponse criar(CorujaUsuarioRequest request) {
        CorujaUsuario entity = CorujaUsuarioMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return CorujaUsuarioMapper.toResponse(repository.save(entity));
    }

    public CorujaUsuarioResponse atualizar(UUID id, CorujaUsuarioRequest request) {
        CorujaUsuario entity = buscarEntidadeAtiva(id);
        CorujaUsuarioMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return CorujaUsuarioMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        CorujaUsuario entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    public CorujaUsuarioResponse alimentar(UUID id, UUID usuarioId) {
        CorujaUsuario coruja = buscarEntidadeAtiva(id);
        if (coruja.getUsuario() == null || !coruja.getUsuario().getId().equals(usuarioId)) {
            throw new OperacaoInvalidaException("Coruja não pertence ao usuário autenticado");
        }

        var viveiro = biscoitoService.obterOuCriarViveiro(coruja.getUsuario());
        biscoitoService.alimentarCorujaComBiscoito(viveiro);

        coruja.setDiasSemBiscoito(0);
        coruja.setFeliz(true);
        return CorujaUsuarioMapper.toResponse(repository.save(coruja));
    }

    private CorujaUsuario buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("CorujaUsuario", id));
    }

    private void aplicarRelacionamentos(CorujaUsuario entity, CorujaUsuarioRequest request) {


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
    }
}