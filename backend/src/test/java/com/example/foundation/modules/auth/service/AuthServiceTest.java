package com.example.foundation.modules.auth.service;

import java.util.Optional;
import java.util.UUID;

import com.example.foundation.modules.auth.dto.LoginRequest;
import com.example.foundation.modules.auth.dto.LoginResponse;
import com.example.foundation.modules.auth.dto.RegistrarRequest;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.domain.enums.UsuarioStatus;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.CredenciaisInvalidasException;
import com.example.foundation.shared.exception.EmailJaCadastradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginComCredenciaisValidas() {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setEmail("joao");
        usuario.setSenhaHash("123");
        usuario.setStatus(UsuarioStatus.ATIVO);

        when(usuarioRepository.findByEmailAndAtivoTrue("joao")).thenReturn(Optional.of(usuario));

        LoginResponse response = authService.login(new LoginRequest("joao", "123"));

        assertThat(response.usuarioId()).isEqualTo(usuarioId);
        assertThat(response.usuario()).isEqualTo("joao");
    }

    @Test
    void loginComSenhaInvalida() {
        Usuario usuario = new Usuario();
        usuario.setEmail("joao");
        usuario.setSenhaHash("123");
        usuario.setStatus(UsuarioStatus.ATIVO);

        when(usuarioRepository.findByEmailAndAtivoTrue("joao")).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> authService.login(new LoginRequest("joao", "errada")))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }

    @Test
    void registrarNovoUsuario() {
        when(usuarioRepository.findByEmailAndAtivoTrue("maria")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(UUID.randomUUID());
            return usuario;
        });

        LoginResponse response = authService.registrar(new RegistrarRequest("maria", "123"));

        assertThat(response.usuario()).isEqualTo("maria");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrarComUsuarioDuplicado() {
        when(usuarioRepository.findByEmailAndAtivoTrue("joao"))
                .thenReturn(Optional.of(new Usuario()));

        assertThatThrownBy(() -> authService.registrar(new RegistrarRequest("joao", "123")))
                .isInstanceOf(EmailJaCadastradoException.class);
    }
}
