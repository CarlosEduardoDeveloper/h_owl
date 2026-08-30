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
@Table(name = "coruja")
public class Coruja extends BaseEntity {

    @Column(name = "nome")
    private String nome;
    @Column(name = "especie")
    private String especie;
    @Enumerated(EnumType.STRING)
    @Column(name = "raridade")
    private Raridade raridade;
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "imagem_url")
    private String imagemUrl;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public Raridade getRaridade() {
        return raridade;
    }

    public void setRaridade(Raridade raridade) {
        this.raridade = raridade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}