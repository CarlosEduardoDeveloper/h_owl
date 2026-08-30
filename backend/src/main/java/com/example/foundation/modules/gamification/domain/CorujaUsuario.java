package com.example.foundation.modules.gamification.domain;

import com.example.foundation.modules.gamification.domain.Coruja;
import com.example.foundation.modules.study.domain.SessaoEstudo;
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
@Table(name = "coruja_usuario")
public class CorujaUsuario extends BaseEntity {

    @Column(name = "adquirida_em")
    private Instant adquiridaEm;
    @Column(name = "nivel")
    private Integer nivel;
    @Column(name = "experiencia")
    private Integer experiencia;
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coruja_id")
    private Coruja coruja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_estudo_id")
    private SessaoEstudo sessaoEstudo;


    public Instant getAdquiridaEm() {
        return adquiridaEm;
    }

    public void setAdquiridaEm(Instant adquiridaEm) {
        this.adquiridaEm = adquiridaEm;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Integer experiencia) {
        this.experiencia = experiencia;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Coruja getCoruja() {
        return coruja;
    }

    public void setCoruja(Coruja coruja) {
        this.coruja = coruja;
    }

    public SessaoEstudo getSessaoEstudo() {
        return sessaoEstudo;
    }

    public void setSessaoEstudo(SessaoEstudo sessaoEstudo) {
        this.sessaoEstudo = sessaoEstudo;
    }
}