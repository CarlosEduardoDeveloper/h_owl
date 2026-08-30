package com.example.foundation.modules.study.service;

import java.util.UUID;

import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.domain.enums.SessaoEstudoStatus;
import com.example.foundation.modules.study.dto.SessaoEstudoConcluirRequest;
import com.example.foundation.modules.study.dto.SessaoEstudoResponse;
import com.example.foundation.modules.study.repository.SessaoEstudoRepository;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.OperacaoInvalidaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoEstudoServiceAcaoTest {

    @Mock
    private SessaoEstudoRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SessaoEstudoService service;

    @Test
    void iniciarSessaoCriada() {
        UUID id = UUID.randomUUID();
        SessaoEstudo sessao = new SessaoEstudo();
        sessao.setId(id);
        sessao.setStatus(SessaoEstudoStatus.CRIADA);

        when(repository.findByIdAndAtivoTrue(id)).thenReturn(java.util.Optional.of(sessao));
        when(repository.save(any(SessaoEstudo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SessaoEstudoResponse response = service.iniciar(id);

        assertThat(response.status()).isEqualTo(SessaoEstudoStatus.EM_ANDAMENTO);
        assertThat(response.inicioEm()).isNotNull();
    }

    @Test
    void concluirSessaoEmAndamento() {
        UUID id = UUID.randomUUID();
        SessaoEstudo sessao = new SessaoEstudo();
        sessao.setId(id);
        sessao.setStatus(SessaoEstudoStatus.EM_ANDAMENTO);

        when(repository.findByIdAndAtivoTrue(id)).thenReturn(java.util.Optional.of(sessao));
        when(repository.save(any(SessaoEstudo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SessaoEstudoResponse response = service.concluir(id, new SessaoEstudoConcluirRequest(25));

        assertThat(response.status()).isEqualTo(SessaoEstudoStatus.CONCLUIDA);
        assertThat(response.duracaoRealMinutos()).isEqualTo(25);
        assertThat(response.fimEm()).isNotNull();
    }

    @Test
    void iniciarSessaoJaIniciadaFalha() {
        UUID id = UUID.randomUUID();
        SessaoEstudo sessao = new SessaoEstudo();
        sessao.setId(id);
        sessao.setStatus(SessaoEstudoStatus.EM_ANDAMENTO);

        when(repository.findByIdAndAtivoTrue(id)).thenReturn(java.util.Optional.of(sessao));

        assertThatThrownBy(() -> service.iniciar(id)).isInstanceOf(OperacaoInvalidaException.class);
    }
}
