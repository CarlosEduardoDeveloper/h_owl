package com.example.foundation.modules.user.service;

import java.util.List;
import java.util.UUID;

import com.example.foundation.modules.gamification.repository.OvoUsuarioRepository;
import com.example.foundation.modules.gamification.repository.ViveiroRepository;
import com.example.foundation.modules.learning.domain.ProgressoTrilha;
import com.example.foundation.modules.learning.domain.Trilha;
import com.example.foundation.modules.learning.repository.ProgressoTrilhaRepository;
import com.example.foundation.modules.learning.repository.TrilhaRepository;
import com.example.foundation.modules.sage.repository.ConsultaSabioRepository;
import com.example.foundation.modules.study.repository.SessaoEstudoRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.dto.MeResumoResponse;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ViveiroRepository viveiroRepository;

    @Mock
    private OvoUsuarioRepository ovoUsuarioRepository;

    @Mock
    private SessaoEstudoRepository sessaoEstudoRepository;

    @Mock
    private ProgressoTrilhaRepository progressoTrilhaRepository;

    @Mock
    private TrilhaRepository trilhaRepository;

    @Mock
    private ConsultaSabioRepository consultaSabioRepository;

    @InjectMocks
    private MeService meService;

    @Test
    void buscarResumoRetornaTrilhasDoUsuario() {
        UUID usuarioId = UUID.randomUUID();
        UUID trilhaId = UUID.randomUUID();

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setEmail("estudante");

        ProgressoTrilha progresso = new ProgressoTrilha();
        progresso.setProgressoPercentual(68);
        Trilha trilha = new Trilha();
        trilha.setId(trilhaId);
        progresso.setTrilha(trilha);

        Trilha trilhaCompleta = new Trilha();
        trilhaCompleta.setId(trilhaId);
        trilhaCompleta.setTitulo("Livro de Mateus");

        when(usuarioRepository.findByIdAndAtivoTrue(usuarioId)).thenReturn(java.util.Optional.of(usuario));
        when(viveiroRepository.findFirstByUsuario_IdAndAtivoTrueOrderByCriadoEmDesc(usuarioId))
                .thenReturn(java.util.Optional.empty());
        when(ovoUsuarioRepository.findFirstByUsuario_IdAndStatusAndAtivoTrueOrderByCriadoEmDesc(
                org.mockito.ArgumentMatchers.eq(usuarioId),
                org.mockito.ArgumentMatchers.any()
        )).thenReturn(java.util.Optional.empty());
        when(sessaoEstudoRepository.findFirstByUsuario_IdAndStatusAndAtivoTrueOrderByCriadoEmDesc(
                org.mockito.ArgumentMatchers.eq(usuarioId),
                org.mockito.ArgumentMatchers.any()
        )).thenReturn(java.util.Optional.empty());
        when(progressoTrilhaRepository.findByUsuario_IdAndAtivoTrueOrderByUltimoAcessoEmDesc(usuarioId))
                .thenReturn(List.of(progresso));
        when(trilhaRepository.findByIdAndAtivoTrue(trilhaId)).thenReturn(java.util.Optional.of(trilhaCompleta));

        MeResumoResponse resumo = meService.buscarResumo(usuarioId);

        assertThat(resumo.usuarioId()).isEqualTo(usuarioId);
        assertThat(resumo.ofensiva()).isNull();
        assertThat(resumo.trilhasEmProgresso()).hasSize(1);
        assertThat(resumo.trilhasEmProgresso().getFirst().titulo()).isEqualTo("Livro de Mateus");
        assertThat(resumo.trilhasEmProgresso().getFirst().progressoPercentual()).isEqualTo(68);
    }
}
