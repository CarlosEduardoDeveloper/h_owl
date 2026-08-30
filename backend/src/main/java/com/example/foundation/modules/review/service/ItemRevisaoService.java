package com.example.foundation.modules.review.service;

import com.example.foundation.modules.review.domain.ItemRevisao;
import com.example.foundation.modules.review.dto.ItemRevisaoRequest;
import com.example.foundation.modules.review.dto.ItemRevisaoResponse;
import com.example.foundation.modules.review.mapper.ItemRevisaoMapper;
import com.example.foundation.modules.review.repository.ItemRevisaoRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemRevisaoService {

    private final ItemRevisaoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public ItemRevisaoService(
            ItemRevisaoRepository repository,
            UsuarioRepository usuarioRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<ItemRevisaoResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(ItemRevisaoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ItemRevisaoResponse buscarAtivo(UUID id) {
        return ItemRevisaoMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public ItemRevisaoResponse criar(ItemRevisaoRequest request) {
        ItemRevisao entity = ItemRevisaoMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return ItemRevisaoMapper.toResponse(repository.save(entity));
    }

    public ItemRevisaoResponse atualizar(UUID id, ItemRevisaoRequest request) {
        ItemRevisao entity = buscarEntidadeAtiva(id);
        ItemRevisaoMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return ItemRevisaoMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        ItemRevisao entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private ItemRevisao buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("ItemRevisao", id));
    }

    private void aplicarRelacionamentos(ItemRevisao entity, ItemRevisaoRequest request) {


        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(request.usuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
            entity.setUsuario(usuario);
        } else {
            entity.setUsuario(null);
        }
    }
}