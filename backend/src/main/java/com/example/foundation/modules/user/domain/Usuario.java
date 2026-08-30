package com.example.foundation.modules.user.domain;

import com.example.foundation.modules.user.domain.enums.UsuarioStatus;
import com.example.foundation.modules.user.domain.Pessoa;
import com.example.foundation.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario extends BaseEntity {

    @Column(name = "email")
    private String email;
    @Column(name = "senha_hash")
    private String senhaHash;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UsuarioStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "ultimo_estudo_em")
    private java.time.LocalDate ultimoEstudoEm;

    @Column(name = "streak_atual")
    private Integer streakAtual;

    @Column(name = "melhor_streak")
    private Integer melhorStreak;

    @Column(name = "ultima_verificacao_diaria")
    private java.time.LocalDate ultimaVerificacaoDiaria;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public UsuarioStatus getStatus() {
        return status;
    }

    public void setStatus(UsuarioStatus status) {
        this.status = status;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public java.time.LocalDate getUltimoEstudoEm() {
        return ultimoEstudoEm;
    }

    public void setUltimoEstudoEm(java.time.LocalDate ultimoEstudoEm) {
        this.ultimoEstudoEm = ultimoEstudoEm;
    }

    public Integer getStreakAtual() {
        return streakAtual;
    }

    public void setStreakAtual(Integer streakAtual) {
        this.streakAtual = streakAtual;
    }

    public Integer getMelhorStreak() {
        return melhorStreak;
    }

    public void setMelhorStreak(Integer melhorStreak) {
        this.melhorStreak = melhorStreak;
    }

    public java.time.LocalDate getUltimaVerificacaoDiaria() {
        return ultimaVerificacaoDiaria;
    }

    public void setUltimaVerificacaoDiaria(java.time.LocalDate ultimaVerificacaoDiaria) {
        this.ultimaVerificacaoDiaria = ultimaVerificacaoDiaria;
    }
}