package com.example.foundation.modules.user.domain;

import com.example.foundation.modules.user.domain.enums.TemaAplicacao;
import com.example.foundation.modules.user.domain.Usuario;
import com.example.foundation.shared.domain.BaseEntity;
import com.example.foundation.shared.domain.enums.ModoFoco;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "preferencia_usuario")
public class PreferenciaUsuario extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tema")
    private TemaAplicacao tema;
    @Enumerated(EnumType.STRING)
    @Column(name = "modo_foco_padrao")
    private ModoFoco modoFocoPadrao;
    @Column(name = "notificacoes_ativas")
    private Boolean notificacoesAtivas;
    @Column(name = "duracao_foco_padrao")
    private Integer duracaoFocoPadrao;
    @Column(name = "versao_biblia_preferida")
    private String versaoBibliaPreferida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    public TemaAplicacao getTema() {
        return tema;
    }

    public void setTema(TemaAplicacao tema) {
        this.tema = tema;
    }

    public ModoFoco getModoFocoPadrao() {
        return modoFocoPadrao;
    }

    public void setModoFocoPadrao(ModoFoco modoFocoPadrao) {
        this.modoFocoPadrao = modoFocoPadrao;
    }

    public Boolean getNotificacoesAtivas() {
        return notificacoesAtivas;
    }

    public void setNotificacoesAtivas(Boolean notificacoesAtivas) {
        this.notificacoesAtivas = notificacoesAtivas;
    }

    public Integer getDuracaoFocoPadrao() {
        return duracaoFocoPadrao;
    }

    public void setDuracaoFocoPadrao(Integer duracaoFocoPadrao) {
        this.duracaoFocoPadrao = duracaoFocoPadrao;
    }

    public String getVersaoBibliaPreferida() {
        return versaoBibliaPreferida;
    }

    public void setVersaoBibliaPreferida(String versaoBibliaPreferida) {
        this.versaoBibliaPreferida = versaoBibliaPreferida;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}