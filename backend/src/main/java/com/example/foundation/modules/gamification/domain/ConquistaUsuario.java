package com.example.foundation.modules.gamification.domain;

import com.example.foundation.modules.gamification.domain.Conquista;
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
@Table(name = "conquista_usuario")
public class ConquistaUsuario extends BaseEntity {

    @Column(name = "conquistada_em")
    private Instant conquistadaEm;
    @Column(name = "progresso")
    private Integer progresso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conquista_id")
    private Conquista conquista;


    public Instant getConquistadaEm() {
        return conquistadaEm;
    }

    public void setConquistadaEm(Instant conquistadaEm) {
        this.conquistadaEm = conquistadaEm;
    }

    public Integer getProgresso() {
        return progresso;
    }

    public void setProgresso(Integer progresso) {
        this.progresso = progresso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Conquista getConquista() {
        return conquista;
    }

    public void setConquista(Conquista conquista) {
        this.conquista = conquista;
    }
}