package com.example.foundation.modules.auth.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import com.example.foundation.modules.auth.controller.AuthController;
import com.example.foundation.modules.auth.service.AuthService;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.shared.exception.ApiErrorResponse;
import com.example.foundation.shared.exception.CredenciaisInvalidasException;
import com.example.foundation.shared.exception.NaoAutenticadoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthFilter extends OncePerRequestFilter {

    private static final List<String> CAMINHOS_PUBLICOS = List.of(
            "/actuator",
            "/api/v1/system",
            "/api/v1/auth/login",
            "/api/v1/auth/registrar",
            "/callback"
    );

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public AuthFilter(AuthService authService, ObjectMapper objectMapper) {
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || isPublico(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String usuario = request.getHeader(AuthController.HEADER_USUARIO);
            String senha = request.getHeader(AuthController.HEADER_SENHA);
            if (usuario == null || usuario.isBlank() || senha == null || senha.isBlank()) {
                throw new NaoAutenticadoException("Usuário e senha são obrigatórios");
            }

            Usuario autenticado = authService.autenticar(usuario, senha);
            request.setAttribute(AuthController.ATRIBUTO_USUARIO_ID, autenticado.getId());
            filterChain.doFilter(request, response);
        } catch (NaoAutenticadoException | CredenciaisInvalidasException exception) {
            responderNaoAutenticado(response, request.getRequestURI(), exception.getMessage());
        }
    }

    private boolean isPublico(String path) {
        return CAMINHOS_PUBLICOS.stream().anyMatch(path::startsWith);
    }

    private void responderNaoAutenticado(HttpServletResponse response, String path, String message)
            throws IOException {
        ApiErrorResponse body = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                message,
                path,
                java.util.Map.of()
        );
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
