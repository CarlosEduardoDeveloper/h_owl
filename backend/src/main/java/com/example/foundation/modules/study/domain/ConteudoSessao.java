package com.example.foundation.modules.study.domain;

import com.example.foundation.modules.study.domain.enums.TipoConteudoSessao;
import com.example.foundation.modules.study.domain.SessaoEstudo;
import com.example.foundation.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "conteudo_sessao")
public class ConteudoSessao extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoConteudoSessao tipo;
    @Column(name = "referencia_id")
    private UUID referenciaId;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "ordem")
    private Integer ordem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_estudo_id")
    private SessaoEstudo sessaoEstudo;


    public TipoConteudoSessao getTipo() {
        return tipo;
    }

    public void setTipo(TipoConteudoSessao tipo) {
        this.tipo = tipo;
    }

    public UUID getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(UUID referenciaId) {
        this.referenciaId = referenciaId;
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

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public SessaoEstudo getSessaoEstudo() {
        return sessaoEstudo;
    }

    public void setSessaoEstudo(SessaoEstudo sessaoEstudo) {
        this.sessaoEstudo = sessaoEstudo;
    }
}