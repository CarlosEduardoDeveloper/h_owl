package com.example.foundation.modules.auth.service;

import java.util.Objects;
import java.util.UUID;

import com.example.foundation.modules.auth.dto.LoginRequest;
import com.example.foundation.modules.auth.dto.LoginResponse;
import com.example.foundation.modules.auth.dto.RegistrarRequest;
import com.example.foundation.modules.auth.dto.SessaoResponse;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.domain.enums.UsuarioStatus;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.CredenciaisInvalidasException;
import com.example.foundation.shared.exception.EmailJaCadastradoException;
import com.example.foundation.shared.exception.NaoAutenticadoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = autenticar(request.usuario(), request.senha());
        return toLoginResponse(usuario);
    }

    @Transactional
    public LoginResponse registrar(RegistrarRequest request) {
        String usuario = normalizarUsuario(request.usuario());
        if (usuarioRepository.findByEmailAndAtivoTrue(usuario).isPresent()) {
            throw new EmailJaCadastradoException();
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(usuario);
        novoUsuario.setSenhaHash(request.senha());
        novoUsuario.setStatus(UsuarioStatus.ATIVO);

        return toLoginResponse(usuarioRepository.save(novoUsuario));
    }

    @Transactional(readOnly = true)
    public SessaoResponse buscarSessao(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(usuarioId)
                .orElseThrow(() -> new NaoAutenticadoException("Usuário não encontrado"));

        return new SessaoResponse(usuario.getId(), usuario.getEmail(), usuario.getStatus());
    }

    @Transactional(readOnly = true)
    public Usuario autenticar(String usuario, String senha) {
        Usuario encontrado = usuarioRepository.findByEmailAndAtivoTrue(normalizarUsuario(usuario))
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!Objects.equals(encontrado.getSenhaHash(), senha) || encontrado.getStatus() != UsuarioStatus.ATIVO) {
            throw new CredenciaisInvalidasException();
        }

        return encontrado;
    }

    private LoginResponse toLoginResponse(Usuario usuario) {
        return new LoginResponse(usuario.getId(), usuario.getEmail());
    }

    private String normalizarUsuario(String usuario) {
        return usuario.trim().toLowerCase();
    }
}
