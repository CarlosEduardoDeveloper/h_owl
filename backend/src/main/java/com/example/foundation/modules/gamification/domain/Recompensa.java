package com.example.foundation.modules.gamification.domain;

import com.example.foundation.modules.gamification.domain.Coruja;
import com.example.foundation.modules.gamification.domain.enums.TipoRecompensa;
import com.example.foundation.modules.gamification.domain.OvoUsuario;
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
@Table(name = "recompensa")
public class Recompensa extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoRecompensa tipo;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "concedida_em")
    private Instant concedidaEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coruja_id")
    private Coruja coruja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_estudo_id")
    private SessaoEstudo sessaoEstudo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ovo_usuario_id")
    private OvoUsuario ovoUsuario;


    public TipoRecompensa getTipo() {
        return tipo;
    }

    public void setTipo(TipoRecompensa tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instant getConcedidaEm() {
        return concedidaEm;
    }

    public void setConcedidaEm(Instant concedidaEm) {
        this.concedidaEm = concedidaEm;
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

    public OvoUsuario getOvoUsuario() {
        return ovoUsuario;
    }

    public void setOvoUsuario(OvoUsuario ovoUsuario) {
        this.ovoUsuario = ovoUsuario;
    }
}