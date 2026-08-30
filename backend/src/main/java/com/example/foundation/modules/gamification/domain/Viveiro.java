package com.example.foundation.modules.gamification.domain;

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

@Entity
@Table(name = "viveiro")
public class Viveiro extends BaseEntity {

    @Column(name = "nome")
    private String nome;
    @Column(name = "nivel")
    private Integer nivel;
    @Column(name = "xp_total")
    private Long xpTotal;
    @Column(name = "tema_visual")
    private String temaVisual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Long getXpTotal() {
        return xpTotal;
    }

    public void setXpTotal(Long xpTotal) {
        this.xpTotal = xpTotal;
    }

    public String getTemaVisual() {
        return temaVisual;
    }

    public void setTemaVisual(String temaVisual) {
        this.temaVisual = temaVisual;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}