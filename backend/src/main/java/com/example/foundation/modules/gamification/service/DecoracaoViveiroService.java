package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.DecoracaoViveiro;
import com.example.foundation.modules.gamification.domain.Viveiro;
import com.example.foundation.modules.gamification.dto.DecoracaoViveiroRequest;
import com.example.foundation.modules.gamification.dto.DecoracaoViveiroResponse;
import com.example.foundation.modules.gamification.mapper.DecoracaoViveiroMapper;
import com.example.foundation.modules.gamification.repository.DecoracaoViveiroRepository;
import com.example.foundation.modules.gamification.repository.ViveiroRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DecoracaoViveiroService {

    private final DecoracaoViveiroRepository repository;
    private final ViveiroRepository viveiroRepository;

    public DecoracaoViveiroService(
            DecoracaoViveiroRepository repository,
            ViveiroRepository viveiroRepository
    ) {
        this.repository = repository;
        this.viveiroRepository = viveiroRepository;
    }

    @Transactional(readOnly = true)
    public List<DecoracaoViveiroResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(DecoracaoViveiroMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DecoracaoViveiroResponse buscarAtivo(UUID id) {
        return DecoracaoViveiroMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public DecoracaoViveiroResponse criar(DecoracaoViveiroRequest request) {
        DecoracaoViveiro entity = DecoracaoViveiroMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return DecoracaoViveiroMapper.toResponse(repository.save(entity));
    }

    public DecoracaoViveiroResponse atualizar(UUID id, DecoracaoViveiroRequest request) {
        DecoracaoViveiro entity = buscarEntidadeAtiva(id);
        DecoracaoViveiroMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return DecoracaoViveiroMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        DecoracaoViveiro entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private DecoracaoViveiro buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("DecoracaoViveiro", id));
    }

    private void aplicarRelacionamentos(DecoracaoViveiro entity, DecoracaoViveiroRequest request) {


        if (request.viveiroId() != null) {
            Viveiro viveiro = viveiroRepository.findByIdAndAtivoTrue(request.viveiroId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Viveiro", request.viveiroId()));
            entity.setViveiro(viveiro);
        } else {
            entity.setViveiro(null);
        }
    }
}