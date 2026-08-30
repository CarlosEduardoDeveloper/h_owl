package com.example.foundation.modules.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.user.domain.Pessoa;
import com.example.foundation.modules.user.dto.PessoaRequest;
import com.example.foundation.modules.user.repository.PessoaRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaService pessoaService;

    @Test
    void excluirAplicaSoftDelete() {
        UUID id = UUID.randomUUID();
        Pessoa pessoa = new Pessoa();
        pessoa.setId(id);
        pessoa.setAtivo(true);
        pessoa.setCriadoEm(Instant.now());

        when(pessoaRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        pessoaService.excluir(id);

        ArgumentCaptor<Pessoa> captor = ArgumentCaptor.forClass(Pessoa.class);
        verify(pessoaRepository).save(captor.capture());

        Pessoa salva = captor.getValue();
        assertThat(salva.getAtivo()).isFalse();
        assertThat(salva.getExcluidoEm()).isNotNull();
    }

    @Test
    void buscarAtivoInexistenteLancaExcecao() {
        UUID id = UUID.randomUUID();
        when(pessoaRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pessoaService.buscarAtivo(id))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    void criarPersisteEntidade() {
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(invocation -> {
            Pessoa pessoa = invocation.getArgument(0);
            pessoa.setId(UUID.randomUUID());
            return pessoa;
        });

        var response = pessoaService.criar(new PessoaRequest("Maria", null, null, null));

        assertThat(response.id()).isNotNull();
        assertThat(response.nome()).isEqualTo("Maria");
        verify(pessoaRepository).save(any(Pessoa.class));
    }
}
