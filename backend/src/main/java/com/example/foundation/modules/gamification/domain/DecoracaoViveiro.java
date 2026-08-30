package com.example.foundation.modules.gamification.domain;

import com.example.foundation.modules.gamification.domain.enums.TipoDecoracao;
import com.example.foundation.modules.gamification.domain.Viveiro;
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
@Table(name = "decoracao_viveiro")
public class DecoracaoViveiro extends BaseEntity {

    @Column(name = "nome")
    private String nome;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoDecoracao tipo;
    @Column(name = "imagem_url")
    private String imagemUrl;
    @Column(name = "adquirida_em")
    private Instant adquiridaEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viveiro_id")
    private Viveiro viveiro;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoDecoracao getTipo() {
        return tipo;
    }

    public void setTipo(TipoDecoracao tipo) {
        this.tipo = tipo;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public Instant getAdquiridaEm() {
        return adquiridaEm;
    }

    public void setAdquiridaEm(Instant adquiridaEm) {
        this.adquiridaEm = adquiridaEm;
    }

    public Viveiro getViveiro() {
        return viveiro;
    }

    public void setViveiro(Viveiro viveiro) {
        this.viveiro = viveiro;
    }
}