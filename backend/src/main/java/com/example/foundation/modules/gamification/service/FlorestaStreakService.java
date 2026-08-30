package com.example.foundation.modules.gamification.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import com.example.foundation.modules.gamification.domain.enums.SaudeFloresta;
import com.example.foundation.modules.user.domain.Usuario;
import org.springframework.stereotype.Service;

@Service
public class FlorestaStreakService {

    public static final String MENSAGEM_ARVORE_ATENCAO = "Sua árvore precisa de atenção";

    public ZoneId zoneIdOf(Usuario usuario) {
        String timezone = usuario.getTimezone();
        if (timezone == null || timezone.isBlank()) {
            return ZoneId.of("America/Fortaleza");
        }
        try {
            return ZoneId.of(timezone.trim());
        } catch (Exception exception) {
            return ZoneId.of("America/Fortaleza");
        }
    }

    public LocalDate hoje(Usuario usuario) {
        return LocalDate.now(zoneIdOf(usuario));
    }

    public void registrarEstudoConcluido(Usuario usuario) {
        LocalDate hoje = hoje(usuario);
        LocalDate ultimo = usuario.getUltimoEstudoEm();

        if (ultimo == null) {
            usuario.setStreakAtual(1);
        } else if (!ultimo.equals(hoje)) {
            long diasSemEstudar = ChronoUnit.DAYS.between(ultimo, hoje);
            int streak = usuario.getStreakAtual() != null ? usuario.getStreakAtual() : 0;
            if (diasSemEstudar <= 2) {
                usuario.setStreakAtual(streak + 1);
            } else {
                usuario.setStreakAtual(streak + 1);
            }
        }

        Integer streakAtual = usuario.getStreakAtual();
        Integer melhor = usuario.getMelhorStreak();
        if (streakAtual != null && (melhor == null || streakAtual > melhor)) {
            usuario.setMelhorStreak(streakAtual);
        }

        usuario.setUltimoEstudoEm(hoje);
    }

    public long diasSemEstudar(Usuario usuario) {
        LocalDate ultimo = usuario.getUltimoEstudoEm();
        if (ultimo == null) {
            return Long.MAX_VALUE;
        }
        return ChronoUnit.DAYS.between(ultimo, hoje(usuario));
    }

    public SaudeFloresta calcularSaudeFloresta(Usuario usuario) {
        long dias = diasSemEstudar(usuario);
        if (dias <= 1) {
            return SaudeFloresta.NORMAL;
        }
        if (dias == 2) {
            return SaudeFloresta.AMARELA;
        }
        if (dias == 3) {
            return SaudeFloresta.CINZA;
        }
        return SaudeFloresta.SUJA;
    }

    public String mensagemArvore(Usuario usuario) {
        long dias = diasSemEstudar(usuario);
        if (dias >= 2) {
            return MENSAGEM_ARVORE_ATENCAO;
        }
        return null;
    }
}
