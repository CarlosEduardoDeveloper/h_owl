package com.example.foundation.modules.user.service;

import java.util.List;
import java.util.UUID;

import com.example.foundation.modules.gamification.domain.OvoUsuario;
import com.example.foundation.modules.gamification.domain.Viveiro;
import com.example.foundation.modules.gamification.domain.enums.OvoStatus;
import com.example.foundation.modules.gamification.domain.enums.SaudeFloresta;
import com.example.foundation.modules.gamification.repository.OvoUsuarioRepository;
import com.example.foundation.modules.gamification.repository.ViveiroRepository;
import com.example.foundation.modules.gamification.service.BiscoitoService;
import com.example.foundation.modules.gamification.service.FlorestaStreakService;
import com.example.foundation.modules.learning.domain.ProgressoTrilha;
import com.example.foundation.modules.learning.domain.Trilha;
import com.example.foundation.modules.learning.repository.ProgressoTrilhaRepository;
import com.example.foundation.modules.learning.repository.TrilhaRepository;
import com.example.foundation.modules.sage.dto.ConsultaSabioResponse;
import com.example.foundation.modules.sage.mapper.ConsultaSabioMapper;
import com.example.foundation.modules.sage.repository.ConsultaSabioRepository;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.domain.enums.SessaoEstudoStatus;
import com.example.foundation.modules.study.repository.SessaoEstudoRepository;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.dto.MeResumoResponse;
import com.example.foundation.modules.user.dto.MeResumoResponse.MeOvoResumo;
import com.example.foundation.modules.user.dto.MeResumoResponse.MeSessaoResumo;
import com.example.foundation.modules.user.dto.MeResumoResponse.MeTrilhaProgressoResumo;
import com.example.foundation.modules.user.dto.MeResumoResponse.MeViveiroResumo;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeService {

    private final UsuarioRepository usuarioRepository;
    private final ViveiroRepository viveiroRepository;
    private final OvoUsuarioRepository ovoUsuarioRepository;
    private final SessaoEstudoRepository sessaoEstudoRepository;
    private final ProgressoTrilhaRepository progressoTrilhaRepository;
    private final TrilhaRepository trilhaRepository;
    private final ConsultaSabioRepository consultaSabioRepository;
    private final FlorestaStreakService florestaStreakService;
    private final BiscoitoService biscoitoService;

    public MeService(
            UsuarioRepository usuarioRepository,
            ViveiroRepository viveiroRepository,
            OvoUsuarioRepository ovoUsuarioRepository,
            SessaoEstudoRepository sessaoEstudoRepository,
            ProgressoTrilhaRepository progressoTrilhaRepository,
            TrilhaRepository trilhaRepository,
            ConsultaSabioRepository consultaSabioRepository,
            FlorestaStreakService florestaStreakService,
            BiscoitoService biscoitoService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.viveiroRepository = viveiroRepository;
        this.ovoUsuarioRepository = ovoUsuarioRepository;
        this.sessaoEstudoRepository = sessaoEstudoRepository;
        this.progressoTrilhaRepository = progressoTrilhaRepository;
        this.trilhaRepository = trilhaRepository;
        this.consultaSabioRepository = consultaSabioRepository;
        this.florestaStreakService = florestaStreakService;
        this.biscoitoService = biscoitoService;
    }

    @Transactional(readOnly = true)
    public MeResumoResponse buscarResumo(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", usuarioId));

        MeViveiroResumo viveiroResumo = viveiroRepository.findFirstByUsuario_IdAndAtivoTrueOrderByCriadoEmDesc(usuarioId)
                .map(this::toViveiroResumo)
                .orElse(null);

        int saldoBiscoitos = viveiroResumo != null && viveiroResumo.saldoBiscoitos() != null
                ? viveiroResumo.saldoBiscoitos()
                : biscoitoService.obterOuCriarViveiro(usuario).getSaldoBiscoitos();

        SaudeFloresta saudeFloresta = florestaStreakService.calcularSaudeFloresta(usuario);
        String mensagemArvore = florestaStreakService.mensagemArvore(usuario);

        MeOvoResumo ovo = ovoUsuarioRepository
                .findFirstByUsuario_IdAndStatusAndAtivoTrueOrderByCriadoEmDesc(usuarioId, OvoStatus.INCUBANDO)
                .map(this::toOvoResumo)
                .orElse(null);

        MeSessaoResumo sessao = sessaoEstudoRepository
                .findFirstByUsuario_IdAndStatusAndAtivoTrueOrderByCriadoEmDesc(
                        usuarioId,
                        SessaoEstudoStatus.EM_ANDAMENTO
                )
                .map(this::toSessaoResumo)
                .orElse(null);

        List<MeTrilhaProgressoResumo> trilhas = progressoTrilhaRepository
                .findByUsuario_IdAndAtivoTrueOrderByUltimoAcessoEmDesc(usuarioId)
                .stream()
                .map(this::toTrilhaProgressoResumo)
                .toList();

        return new MeResumoResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getStreakAtual(),
                null,
                null,
                saldoBiscoitos,
                saudeFloresta,
                mensagemArvore,
                viveiroResumo,
                ovo,
                sessao,
                trilhas
        );
    }

    @Transactional(readOnly = true)
    public List<ConsultaSabioResponse> listarConsultasSabio(UUID usuarioId) {
        validarUsuario(usuarioId);
        return consultaSabioRepository.findByUsuario_IdAndAtivoTrueOrderByCriadoEmAsc(usuarioId)
                .stream()
                .map(ConsultaSabioMapper::toResponse)
                .toList();
    }

    private void validarUsuario(UUID usuarioId) {
        if (!usuarioRepository.findByIdAndAtivoTrue(usuarioId).isPresent()) {
            throw new RecursoNaoEncontradoException("Usuario", usuarioId);
        }
    }

    private MeViveiroResumo toViveiroResumo(Viveiro viveiro) {
        return new MeViveiroResumo(
                viveiro.getId(),
                viveiro.getNome(),
                viveiro.getNivel(),
                viveiro.getXpTotal(),
                viveiro.getSaldoBiscoitos()
        );
    }

    private MeOvoResumo toOvoResumo(OvoUsuario ovo) {
        return new MeOvoResumo(ovo.getId(), ovo.getStatus());
    }

    private MeSessaoResumo toSessaoResumo(SessaoEstudo sessao) {
        return new MeSessaoResumo(sessao.getId(), sessao.getStatus(), sessao.getIntencao());
    }

    private MeTrilhaProgressoResumo toTrilhaProgressoResumo(ProgressoTrilha progresso) {
        UUID trilhaId = progresso.getTrilha() != null ? progresso.getTrilha().getId() : null;
        String titulo = "Trilha";
        if (trilhaId != null) {
            titulo = trilhaRepository.findByIdAndAtivoTrue(trilhaId)
                    .map(Trilha::getTitulo)
                    .orElse(titulo);
        }
        return new MeTrilhaProgressoResumo(trilhaId, titulo, progresso.getProgressoPercentual());
    }
}
