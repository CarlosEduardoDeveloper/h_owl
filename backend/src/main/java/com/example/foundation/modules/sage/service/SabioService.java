package com.example.foundation.modules.sage.service;

import java.util.UUID;

import com.example.foundation.modules.sage.domain.ConsultaSabio;
import com.example.foundation.modules.sage.dto.ConsultaSabioResponse;
import com.example.foundation.modules.sage.dto.PerguntarSabioRequest;
import com.example.foundation.modules.sage.mapper.ConsultaSabioMapper;
import com.example.foundation.modules.sage.repository.ConsultaSabioRepository;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.repository.SessaoEstudoRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SabioService {

    private static final String RESPOSTA_MOCK =
            "Obrigado pela sua pergunta. A integração com IA será disponibilizada em breve. "
                    + "Continue refletindo sobre o texto em estudo e registre suas anotações.";

    private final ConsultaSabioRepository consultaSabioRepository;
    private final UsuarioRepository usuarioRepository;
    private final SessaoEstudoRepository sessaoEstudoRepository;

    public SabioService(
            ConsultaSabioRepository consultaSabioRepository,
            UsuarioRepository usuarioRepository,
            SessaoEstudoRepository sessaoEstudoRepository
    ) {
        this.consultaSabioRepository = consultaSabioRepository;
        this.usuarioRepository = usuarioRepository;
        this.sessaoEstudoRepository = sessaoEstudoRepository;
    }

    @Transactional
    public ConsultaSabioResponse perguntar(UUID usuarioId, PerguntarSabioRequest request) {
        Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", usuarioId));

        ConsultaSabio consulta = new ConsultaSabio();
        consulta.setUsuario(usuario);
        consulta.setPergunta(request.pergunta());
        consulta.setContextoReferencia(request.contextoReferencia());
        consulta.setResposta(RESPOSTA_MOCK);

        if (request.sessaoEstudoId() != null) {
            SessaoEstudo sessao = sessaoEstudoRepository.findByIdAndAtivoTrue(request.sessaoEstudoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("SessaoEstudo", request.sessaoEstudoId()));
            consulta.setSessaoEstudo(sessao);
        }

        return ConsultaSabioMapper.toResponse(consultaSabioRepository.save(consulta));
    }
}
