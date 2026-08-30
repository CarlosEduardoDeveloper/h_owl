package com.example.foundation.modules.user.service;

import com.example.foundation.modules.user.domain.Pessoa;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.dto.UsuarioRequest;
import com.example.foundation.modules.user.dto.UsuarioResponse;
import com.example.foundation.modules.user.mapper.UsuarioMapper;
import com.example.foundation.modules.user.repository.PessoaRepository;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PessoaRepository pessoaRepository;

    public UsuarioService(
            UsuarioRepository repository,
            PessoaRepository pessoaRepository
    ) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(UsuarioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarAtivo(UUID id) {
        return UsuarioMapper.toResponse(buscarEntidadeAtiva(id));
    }

    public UsuarioResponse criar(UsuarioRequest request) {
        Usuario entity = UsuarioMapper.toEntity(request);
        aplicarRelacionamentos(entity, request);
        return UsuarioMapper.toResponse(repository.save(entity));
    }

    public UsuarioResponse atualizar(UUID id, UsuarioRequest request) {
        Usuario entity = buscarEntidadeAtiva(id);
        UsuarioMapper.applyRequest(entity, request);
        aplicarRelacionamentos(entity, request);
        return UsuarioMapper.toResponse(repository.save(entity));
    }

    public void excluir(UUID id) {
        Usuario entity = buscarEntidadeAtiva(id);
        entity.excluirLogicamente();
        repository.save(entity);
    }

    private Usuario buscarEntidadeAtiva(UUID id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", id));
    }

    private void aplicarRelacionamentos(Usuario entity, UsuarioRequest request) {


        if (request.pessoaId() != null) {
            Pessoa pessoa = pessoaRepository.findByIdAndAtivoTrue(request.pessoaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa", request.pessoaId()));
            entity.setPessoa(pessoa);
        } else {
            entity.setPessoa(null);
        }
    }
}