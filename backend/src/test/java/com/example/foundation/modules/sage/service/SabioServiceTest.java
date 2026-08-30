package com.example.foundation.modules.sage.service;

import java.util.UUID;

import com.example.foundation.modules.sage.domain.ConsultaSabio;
import com.example.foundation.modules.sage.dto.ConsultaSabioResponse;
import com.example.foundation.modules.sage.dto.PerguntarSabioRequest;
import com.example.foundation.modules.sage.repository.ConsultaSabioRepository;
import com.example.foundation.modules.study.repository.SessaoEstudoRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SabioServiceTest {

    @Mock
    private ConsultaSabioRepository consultaSabioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SessaoEstudoRepository sessaoEstudoRepository;

    @InjectMocks
    private SabioService sabioService;

    @Test
    void perguntarPersisteConsultaComRespostaMock() {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setEmail("estudante");

        when(usuarioRepository.findByIdAndAtivoTrue(usuarioId)).thenReturn(java.util.Optional.of(usuario));
        when(consultaSabioRepository.save(any(ConsultaSabio.class))).thenAnswer(invocation -> {
            ConsultaSabio consulta = invocation.getArgument(0);
            consulta.setId(UUID.randomUUID());
            return consulta;
        });

        ConsultaSabioResponse response = sabioService.perguntar(
                usuarioId,
                new PerguntarSabioRequest("O que significa shalom?", null, null)
        );

        assertThat(response.pergunta()).isEqualTo("O que significa shalom?");
        assertThat(response.resposta()).contains("integração com IA");

        ArgumentCaptor<ConsultaSabio> captor = ArgumentCaptor.forClass(ConsultaSabio.class);
        verify(consultaSabioRepository).save(captor.capture());
        assertThat(captor.getValue().getUsuario().getId()).isEqualTo(usuarioId);
    }
}
