package com.example.foundation.modules.gamification.service;

import com.example.foundation.modules.gamification.domain.Recompensa;
import com.example.foundation.modules.gamification.domain.Viveiro;
import com.example.foundation.modules.gamification.domain.enums.TipoRecompensa;
import com.example.foundation.modules.gamification.repository.RecompensaRepository;
import com.example.foundation.modules.gamification.repository.ViveiroRepository;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.shared.exception.OperacaoInvalidaException;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class BiscoitoService {

    private final ViveiroRepository viveiroRepository;
    private final RecompensaRepository recompensaRepository;

    public BiscoitoService(
            ViveiroRepository viveiroRepository,
            RecompensaRepository recompensaRepository
    ) {
        this.viveiroRepository = viveiroRepository;
        this.recompensaRepository = recompensaRepository;
    }

    public Viveiro obterOuCriarViveiro(Usuario usuario) {
        return viveiroRepository.findFirstByUsuario_IdAndAtivoTrueOrderByCriadoEmDesc(usuario.getId())
                .orElseGet(() -> criarViveiroPadrao(usuario));
    }

    public int saldo(Viveiro viveiro) {
        return viveiro.getSaldoBiscoitos() != null ? viveiro.getSaldoBiscoitos() : 0;
    }

    public boolean concederBiscoitoEstudo(Usuario usuario, SessaoEstudo sessao, String motivo) {
        Viveiro viveiro = obterOuCriarViveiro(usuario);
        int saldoAtual = saldo(viveiro);
        viveiro.setSaldoBiscoitos(saldoAtual + 1);
        viveiroRepository.save(viveiro);

        Recompensa recompensa = new Recompensa();
        recompensa.setUsuario(usuario);
        recompensa.setSessaoEstudo(sessao);
        recompensa.setTipo(TipoRecompensa.BONUS);
        recompensa.setTitulo("Biscoito");
        recompensa.setDescricao(motivo);
        recompensa.setConcedidaEm(Instant.now());
        recompensaRepository.save(recompensa);
        return true;
    }

    public boolean concederBiscoitoQuiz(Usuario usuario, String motivo) {
        Viveiro viveiro = obterOuCriarViveiro(usuario);
        int saldoAtual = saldo(viveiro);
        viveiro.setSaldoBiscoitos(saldoAtual + 1);
        viveiroRepository.save(viveiro);

        Recompensa recompensa = new Recompensa();
        recompensa.setUsuario(usuario);
        recompensa.setTipo(TipoRecompensa.BONUS);
        recompensa.setTitulo("Biscoito");
        recompensa.setDescricao(motivo);
        recompensa.setConcedidaEm(Instant.now());
        recompensaRepository.save(recompensa);
        return true;
    }

    public boolean consumirBiscoitos(Viveiro viveiro, int quantidade) {
        int saldoAtual = saldo(viveiro);
        if (saldoAtual < quantidade) {
            return false;
        }
        viveiro.setSaldoBiscoitos(saldoAtual - quantidade);
        viveiroRepository.save(viveiro);
        return true;
    }

    public void alimentarCorujaComBiscoito(Viveiro viveiro) {
        if (!consumirBiscoitos(viveiro, 1)) {
            throw new OperacaoInvalidaException("Saldo de biscoitos insuficiente");
        }
    }

    private Viveiro criarViveiroPadrao(Usuario usuario) {
        Viveiro viveiro = new Viveiro();
        viveiro.setUsuario(usuario);
        viveiro.setNome("Minha Floresta");
        viveiro.setNivel(1);
        viveiro.setXpTotal(0L);
        viveiro.setSaldoBiscoitos(0);
        viveiro.setTemaVisual("padrao");
        return viveiroRepository.save(viveiro);
    }
}
