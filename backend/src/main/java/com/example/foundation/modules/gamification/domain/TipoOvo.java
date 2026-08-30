package com.example.foundation.modules.gamification.domain;

import com.example.foundation.modules.gamification.domain.enums.Raridade;
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
@Table(name = "tipo_ovo")
public class TipoOvo extends BaseEntity {

    @Column(name = "nome")
    private String nome;
    @Enumerated(EnumType.STRING)
    @Column(name = "raridade")
    private Raridade raridade;
    @Column(name = "duracao_minima_minutos")
    private Integer duracaoMinimaMinutos;
    @Column(name = "duracao_maxima_minutos")
    private Integer duracaoMaximaMinutos;
    @Column(name = "imagem_url")
    private String imagemUrl;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Raridade getRaridade() {
        return raridade;
    }

    public void setRaridade(Raridade raridade) {
        this.raridade = raridade;
    }

    public Integer getDuracaoMinimaMinutos() {
        return duracaoMinimaMinutos;
    }

    public void setDuracaoMinimaMinutos(Integer duracaoMinimaMinutos) {
        this.duracaoMinimaMinutos = duracaoMinimaMinutos;
    }

    public Integer getDuracaoMaximaMinutos() {
        return duracaoMaximaMinutos;
    }

    public void setDuracaoMaximaMinutos(Integer duracaoMaximaMinutos) {
        this.duracaoMaximaMinutos = duracaoMaximaMinutos;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}