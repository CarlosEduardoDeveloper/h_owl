package com.example.foundation.modules.review.domain;

import com.example.foundation.modules.review.domain.enums.TipoItemRevisao;
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
import java.util.UUID;

@Entity
@Table(name = "item_revisao")
public class ItemRevisao extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoItemRevisao tipo;
    @Column(name = "referencia_id")
    private UUID referenciaId;
    @Column(name = "proxima_revisao_em")
    private Instant proximaRevisaoEm;
    @Column(name = "intervalo_dias")
    private Integer intervaloDias;
    @Column(name = "facilidade")
    private Integer facilidade;
    @Column(name = "repeticoes")
    private Integer repeticoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    public TipoItemRevisao getTipo() {
        return tipo;
    }

    public void setTipo(TipoItemRevisao tipo) {
        this.tipo = tipo;
    }

    public UUID getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(UUID referenciaId) {
        this.referenciaId = referenciaId;
    }

    public Instant getProximaRevisaoEm() {
        return proximaRevisaoEm;
    }

    public void setProximaRevisaoEm(Instant proximaRevisaoEm) {
        this.proximaRevisaoEm = proximaRevisaoEm;
    }

    public Integer getIntervaloDias() {
        return intervaloDias;
    }

    public void setIntervaloDias(Integer intervaloDias) {
        this.intervaloDias = intervaloDias;
    }

    public Integer getFacilidade() {
        return facilidade;
    }

    public void setFacilidade(Integer facilidade) {
        this.facilidade = facilidade;
    }

    public Integer getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(Integer repeticoes) {
        this.repeticoes = repeticoes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}