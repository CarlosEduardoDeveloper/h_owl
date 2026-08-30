package com.example.foundation.modules.gamification.domain;

import com.example.foundation.modules.gamification.domain.enums.OvoStatus;
import com.example.foundation.modules.gamification.domain.TipoOvo;
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
@Table(name = "ovo_usuario")
public class OvoUsuario extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OvoStatus status;
    @Column(name = "chocado_em")
    private Instant chocadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_ovo_id")
    private TipoOvo tipoOvo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_estudo_id")
    private SessaoEstudo sessaoEstudo;


    public OvoStatus getStatus() {
        return status;
    }

    public void setStatus(OvoStatus status) {
        this.status = status;
    }

    public Instant getChocadoEm() {
        return chocadoEm;
    }

    public void setChocadoEm(Instant chocadoEm) {
        this.chocadoEm = chocadoEm;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoOvo getTipoOvo() {
        return tipoOvo;
    }

    public void setTipoOvo(TipoOvo tipoOvo) {
        this.tipoOvo = tipoOvo;
    }

    public SessaoEstudo getSessaoEstudo() {
        return sessaoEstudo;
    }

    public void setSessaoEstudo(SessaoEstudo sessaoEstudo) {
        this.sessaoEstudo = sessaoEstudo;
    }
}