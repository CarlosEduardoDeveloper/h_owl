package com.example.foundation.modules.gamification.service;

import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.OvoUsuario;
import com.example.foundation.modules.gamification.domain.enums.OvoStatus;
import com.example.foundation.modules.gamification.repository.OvoUsuarioRepository;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstudoGamificacaoServiceInterromperTest {

    @Mock
    private OvoUsuarioRepository ovoUsuarioRepository;

    @Mock
    private com.example.foundation.modules.gamification.repository.TipoOvoRepository tipoOvoRepository;

    @Mock
    private com.example.foundation.modules.gamification.repository.CorujaRepository corujaRepository;

    @Mock
    private com.example.foundation.modules.gamification.repository.CorujaUsuarioRepository corujaUsuarioRepository;

    @Mock
    private com.example.foundation.modules.user.repository.UsuarioRepository usuarioRepository;

    @Mock
    private BiscoitoService biscoitoService;

    @Mock
    private FlorestaStreakService florestaStreakService;

    @InjectMocks
    private EstudoGamificacaoService service;

    @Test
    void interromperCancelaOvoIncubando() {
        UUID sessaoId = UUID.randomUUID();
        SessaoEstudo sessao = new SessaoEstudo();
        sessao.setId(sessaoId);

        OvoUsuario ovo = new OvoUsuario();
        ovo.setStatus(OvoStatus.INCUBANDO);

        when(ovoUsuarioRepository.findBySessaoEstudo_IdAndAtivoTrue(sessaoId)).thenReturn(Optional.of(ovo));
        when(ovoUsuarioRepository.save(any(OvoUsuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.aoInterromperSessao(sessao);

        verify(ovoUsuarioRepository).save(any(OvoUsuario.class));
        org.assertj.core.api.Assertions.assertThat(ovo.getStatus()).isEqualTo(OvoStatus.CANCELADO);
    }
}
