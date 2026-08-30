package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.Coruja;
import com.example.foundation.modules.gamification.domain.CorujaUsuario;
import com.example.foundation.modules.gamification.domain.OvoUsuario;
import com.example.foundation.modules.gamification.domain.TipoOvo;
import com.example.foundation.modules.gamification.domain.Viveiro;
import com.example.foundation.modules.gamification.domain.enums.OvoStatus;
import com.example.foundation.modules.gamification.dto.GamificacaoSessaoResponse;
import com.example.foundation.modules.gamification.repository.CorujaRepository;
import com.example.foundation.modules.gamification.repository.CorujaUsuarioRepository;
import com.example.foundation.modules.gamification.repository.OvoUsuarioRepository;
import com.example.foundation.modules.gamification.repository.TipoOvoRepository;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.study.domain.enums.IntencaoEstudo;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.modules.user.repository.UsuarioRepository;
import com.example.foundation.shared.exception.OperacaoInvalidaException;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstudoGamificacaoService {

    private static final Set<Integer> DURACOES_PERMITIDAS = Set.of(10, 15, 30);
    private static final int POLEIROS_DISPONIVEIS = 8;

    private final OvoUsuarioRepository ovoUsuarioRepository;
    private final TipoOvoRepository tipoOvoRepository;
    private final CorujaRepository corujaRepository;
    private final CorujaUsuarioRepository corujaUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final BiscoitoService biscoitoService;
    private final FlorestaStreakService florestaStreakService;

    public EstudoGamificacaoService(
            OvoUsuarioRepository ovoUsuarioRepository,
            TipoOvoRepository tipoOvoRepository,
            CorujaRepository corujaRepository,
            CorujaUsuarioRepository corujaUsuarioRepository,
            UsuarioRepository usuarioRepository,
            BiscoitoService biscoitoService,
            FlorestaStreakService florestaStreakService
    ) {
        this.ovoUsuarioRepository = ovoUsuarioRepository;
        this.tipoOvoRepository = tipoOvoRepository;
        this.corujaRepository = corujaRepository;
        this.corujaUsuarioRepository = corujaUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.biscoitoService = biscoitoService;
        this.florestaStreakService = florestaStreakService;
    }

    public void aoIniciarSessao(SessaoEstudo sessao) {
        validarDuracao(sessao.getDuracaoPlanejadaMinutos());

        Usuario usuario = sessao.getUsuario();
        if (usuario == null) {
            throw new OperacaoInvalidaException("Sessão sem usuário associado");
        }

        TipoOvo tipoOvo = tipoOvoRepository
                .findFirstByDuracaoMinimaMinutosAndAtivoTrue(sessao.getDuracaoPlanejadaMinutos())
                .orElseThrow(() -> new OperacaoInvalidaException("Tipo de ovo não configurado para esta duração"));

        OvoUsuario ovo = new OvoUsuario();
        ovo.setUsuario(usuario);
        ovo.setSessaoEstudo(sessao);
        ovo.setTipoOvo(tipoOvo);
        ovo.setStatus(OvoStatus.INCUBANDO);
        ovoUsuarioRepository.save(ovo);
    }

    public GamificacaoSessaoResponse aoConcluirSessao(SessaoEstudo sessao) {
        validarDuracaoConclusao(sessao);

        OvoUsuario ovo = ovoUsuarioRepository.findBySessaoEstudo_IdAndAtivoTrue(sessao.getId())
                .orElseThrow(() -> new OperacaoInvalidaException("Ovo da sessão não encontrado"));

        if (ovo.getStatus() != OvoStatus.INCUBANDO) {
            throw new OperacaoInvalidaException("Ovo não está incubando");
        }

        Usuario usuario = sessao.getUsuario();
        ovo.setStatus(OvoStatus.CHOCADO);
        ovo.setChocadoEm(Instant.now());
        ovoUsuarioRepository.save(ovo);

        CorujaUsuario corujaUsuario = criarCorujaUsuario(usuario, sessao);
        boolean biscoitoConcedido = concederBiscoitoSeAplicavel(usuario, sessao);

        florestaStreakService.registrarEstudoConcluido(usuario);
        usuarioRepository.save(usuario);

        Viveiro viveiro = biscoitoService.obterOuCriarViveiro(usuario);

        return new GamificacaoSessaoResponse(
                ovo.getId(),
                corujaUsuario.getId(),
                corujaUsuario.getCoruja() != null ? corujaUsuario.getCoruja().getNome() : null,
                corujaUsuario.getPoleiroIndice(),
                biscoitoConcedido,
                biscoitoService.saldo(viveiro),
                usuario.getStreakAtual(),
                florestaStreakService.calcularSaudeFloresta(usuario)
        );
    }

    public void aoInterromperSessao(SessaoEstudo sessao) {
        ovoUsuarioRepository.findBySessaoEstudo_IdAndAtivoTrue(sessao.getId()).ifPresent(ovo -> {
            if (ovo.getStatus() == OvoStatus.INCUBANDO) {
                ovo.setStatus(OvoStatus.CANCELADO);
                ovoUsuarioRepository.save(ovo);
            }
        });
    }

    private CorujaUsuario criarCorujaUsuario(Usuario usuario, SessaoEstudo sessao) {
        List<Coruja> catalogo = corujaRepository.findByAtivoTrue();
        if (catalogo.isEmpty()) {
            throw new OperacaoInvalidaException("Catálogo de corujas indisponível");
        }

        Coruja sorteada = catalogo.get(ThreadLocalRandom.current().nextInt(catalogo.size()));

        CorujaUsuario corujaUsuario = new CorujaUsuario();
        corujaUsuario.setUsuario(usuario);
        corujaUsuario.setCoruja(sorteada);
        corujaUsuario.setSessaoEstudo(sessao);
        corujaUsuario.setAdquiridaEm(Instant.now());
        corujaUsuario.setNivel(1);
        corujaUsuario.setExperiencia(0);
        corujaUsuario.setPoleiroIndice(ThreadLocalRandom.current().nextInt(1, POLEIROS_DISPONIVEIS + 1));
        corujaUsuario.setDiasSemBiscoito(0);
        corujaUsuario.setFeliz(true);
        return corujaUsuarioRepository.save(corujaUsuario);
    }

    private boolean concederBiscoitoSeAplicavel(Usuario usuario, SessaoEstudo sessao) {
        IntencaoEstudo intencao = sessao.getIntencao();
        if (intencao == IntencaoEstudo.TRILHA || intencao == IntencaoEstudo.QUIZ) {
            String motivo = intencao == IntencaoEstudo.QUIZ
                    ? "Quiz concluído"
                    : "Estudo direcionado concluído";
            return biscoitoService.concederBiscoitoEstudo(usuario, sessao, motivo);
        }
        return false;
    }

    private void validarDuracao(Integer duracaoMinutos) {
        if (duracaoMinutos == null || !DURACOES_PERMITIDAS.contains(duracaoMinutos)) {
            throw new OperacaoInvalidaException("Duração deve ser 10, 15 ou 30 minutos");
        }
    }

    private void validarDuracaoConclusao(SessaoEstudo sessao) {
        validarDuracao(sessao.getDuracaoPlanejadaMinutos());
        Integer real = sessao.getDuracaoRealMinutos();
        if (real != null && real < sessao.getDuracaoPlanejadaMinutos()) {
            throw new OperacaoInvalidaException("Sessão concluída antes do tempo planejado");
        }
    }
}
