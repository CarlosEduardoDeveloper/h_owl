package com.example.foundation.modules.learning.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.learning.domain.Trilha;
import com.example.foundation.modules.learning.dto.TrilhaRequest;
import com.example.foundation.modules.learning.repository.TrilhaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrilhaServiceCrudTest {

    @Mock
    private TrilhaRepository repository;

    @InjectMocks
    private TrilhaService trilhaService;

    @Test
    void listarRetornaSomenteAtivos() {
        Trilha ativa = new Trilha();
        ativa.setId(UUID.randomUUID());
        ativa.setTitulo("Ativa");
        ativa.setAtivo(true);
        ativa.setCriadoEm(Instant.now());

        when(repository.findByAtivoTrue()).thenReturn(List.of(ativa));

        var resultado = trilhaService.listarAtivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().titulo()).isEqualTo("Ativa");
        verify(repository).findByAtivoTrue();
    }

    @Test
    void excluirAplicaSoftDeleteSemRemoverFisicamente() {
        UUID id = UUID.randomUUID();
        Trilha trilha = new Trilha();
        trilha.setId(id);
        trilha.setAtivo(true);
        trilha.setCriadoEm(Instant.now());

        when(repository.findByIdAndAtivoTrue(id)).thenReturn(Optional.of(trilha));
        when(repository.save(any(Trilha.class))).thenAnswer(invocation -> invocation.getArgument(0));

        trilhaService.excluir(id);

        ArgumentCaptor<Trilha> captor = ArgumentCaptor.forClass(Trilha.class);
        verify(repository).save(captor.capture());
        verify(repository, never()).delete(any());
        verify(repository, never()).deleteById(any());

        Trilha salva = captor.getValue();
        assertThat(salva.getAtivo()).isFalse();
        assertThat(salva.getExcluidoEm()).isNotNull();
    }

    @Test
    void atualizarPreservaIdECriadoEm() {
        UUID id = UUID.randomUUID();
        Instant criadoEm = Instant.parse("2026-01-01T00:00:00Z");
        Trilha trilha = new Trilha();
        trilha.setId(id);
        trilha.setTitulo("Original");
        trilha.setAtivo(true);
        trilha.setCriadoEm(criadoEm);

        when(repository.findByIdAndAtivoTrue(id)).thenReturn(Optional.of(trilha));
        when(repository.save(any(Trilha.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = trilhaService.atualizar(id, new TrilhaRequest("Nova", null, null, null, null));

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.titulo()).isEqualTo("Nova");
        assertThat(response.criadoEm()).isEqualTo(criadoEm);
    }
}
