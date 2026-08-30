package com.example.foundation.modules.learning.domain;

import com.example.foundation.modules.learning.domain.Trilha;
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
@Table(name = "modulo")
public class Modulo extends BaseEntity {

    @Column(name = "titulo")
    private String titulo;
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "ordem")
    private Integer ordem;
    @Column(name = "tempo_sugerido_minutos")
    private Integer tempoSugeridoMinutos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trilha_id")
    private Trilha trilha;


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

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Integer getTempoSugeridoMinutos() {
        return tempoSugeridoMinutos;
    }

    public void setTempoSugeridoMinutos(Integer tempoSugeridoMinutos) {
        this.tempoSugeridoMinutos = tempoSugeridoMinutos;
    }

    public Trilha getTrilha() {
        return trilha;
    }

    public void setTrilha(Trilha trilha) {
        this.trilha = trilha;
    }
}