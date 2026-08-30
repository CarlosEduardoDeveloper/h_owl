package com.example.foundation.modules.study.service;

import com.example.foundation.modules.study.domain.ConteudoSessao;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.dto.ConteudoSessaoRequest;
import com.example.foundation.modules.study.dto.ConteudoSessaoResponse;
import com.example.foundation.modules.study.mapper.ConteudoSessaoMapper;
import com.example.foundation.modules.study.repository.ConteudoSessaoRepository;
import com.example.foundation.modules.study.repository.SessaoEstudoRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConteudoSessaoService {

    private final ConteudoSessaoRepository repository;
    private final SessaoEstudoRepository sessaoEstudoRepository;

    public ConteudoSessaoService(
            ConteudoSessaoRepository repository,
            SessaoEstudoRepository sessaoEstudoRepository
    ) {
        this.repository = repository;
        this.sessaoEstudoRepository = sessaoEstudoRepository;
    }

    @Transactional(readOnly = true)
    public List<ConteudoSessaoResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(ConteudoSessaoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConteudoSessaoResponse buscarAtivo(UUID id) {
        return ConteudoSessaoMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public ConteudoSessaoResponse criar(ConteudoSessaoRequest request) {
        ConteudoSessao entity = ConteudoSessaoMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return ConteudoSessaoMapper.toResponse(repository.save(entity));
    }

    public ConteudoSessaoResponse atualizar(UUID id, ConteudoSessaoRequest request) {
        ConteudoSessao entity = buscarEntidadeAtiva(id);
        ConteudoSessaoMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return ConteudoSessaoMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        ConteudoSessao entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private ConteudoSessao buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("ConteudoSessao", id));
    }

    private void aplicarRelacionamentos(ConteudoSessao entity, ConteudoSessaoRequest request) {


        if (request.sessaoEstudoId() != null) {
            SessaoEstudo sessaoEstudo = sessaoEstudoRepository.findByIdAndAtivoTrue(request.sessaoEstudoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("SessaoEstudo", request.sessaoEstudoId()));
            entity.setSessaoEstudo(sessaoEstudo);
        } else {
            entity.setSessaoEstudo(null);
        }
    }
}