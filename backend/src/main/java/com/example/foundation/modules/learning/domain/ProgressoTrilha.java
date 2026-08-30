package com.example.foundation.modules.learning.domain;

import com.example.foundation.modules.learning.domain.enums.ProgressoTrilhaStatus;
import com.example.foundation.modules.learning.domain.Modulo;
import com.example.foundation.modules.learning.domain.Trilha;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "progresso_trilha")
public class ProgressoTrilha extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProgressoTrilhaStatus status;
    @Column(name = "progresso_percentual")
    private Integer progressoPercentual;
    @Column(name = "ultimo_acesso_em")
    private Instant ultimoAcessoEm;
    @Column(name = "concluido_em")
    private Instant concluidoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trilha_id")
    private Trilha trilha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_atual_id")
    private Modulo moduloAtual;


    public ProgressoTrilhaStatus getStatus() {
        return status;
    }

    public void setStatus(ProgressoTrilhaStatus status) {
        this.status = status;
    }

    public Integer getProgressoPercentual() {
        return progressoPercentual;
    }

    public void setProgressoPercentual(Integer progressoPercentual) {
        this.progressoPercentual = progressoPercentual;
    }

    public Instant getUltimoAcessoEm() {
        return ultimoAcessoEm;
    }

    public void setUltimoAcessoEm(Instant ultimoAcessoEm) {
        this.ultimoAcessoEm = ultimoAcessoEm;
    }

    public Instant getConcluidoEm() {
        return concluidoEm;
    }

    public void setConcluidoEm(Instant concluidoEm) {
        this.concluidoEm = concluidoEm;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Trilha getTrilha() {
        return trilha;
    }

    public void setTrilha(Trilha trilha) {
        this.trilha = trilha;
    }

    public Modulo getModuloAtual() {
        return moduloAtual;
    }

    public void setModuloAtual(Modulo moduloAtual) {
        this.moduloAtual = moduloAtual;
    }
}