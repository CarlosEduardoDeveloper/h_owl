package com.example.foundation.modules.study.domain;

import com.example.foundation.modules.study.domain.enums.IntencaoEstudo;
import com.example.foundation.modules.study.domain.enums.SessaoEstudoStatus;
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
import java.time.Instant;

@Entity
@Table(name = "sessao_estudo")
public class SessaoEstudo extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "intencao")
    private IntencaoEstudo intencao;
    @Enumerated(EnumType.STRING)
    @Column(name = "modo_foco")
    private ModoFoco modoFoco;
    @Column(name = "duracao_planejada_minutos")
    private Integer duracaoPlanejadaMinutos;
    @Column(name = "duracao_real_minutos")
    private Integer duracaoRealMinutos;
    @Column(name = "inicio_em")
    private Instant inicioEm;
    @Column(name = "fim_em")
    private Instant fimEm;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SessaoEstudoStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    public IntencaoEstudo getIntencao() {
        return intencao;
    }

    public void setIntencao(IntencaoEstudo intencao) {
        this.intencao = intencao;
    }

    public ModoFoco getModoFoco() {
        return modoFoco;
    }

    public void setModoFoco(ModoFoco modoFoco) {
        this.modoFoco = modoFoco;
    }

    public Integer getDuracaoPlanejadaMinutos() {
        return duracaoPlanejadaMinutos;
    }

    public void setDuracaoPlanejadaMinutos(Integer duracaoPlanejadaMinutos) {
        this.duracaoPlanejadaMinutos = duracaoPlanejadaMinutos;
    }

    public Integer getDuracaoRealMinutos() {
        return duracaoRealMinutos;
    }

    public void setDuracaoRealMinutos(Integer duracaoRealMinutos) {
        this.duracaoRealMinutos = duracaoRealMinutos;
    }

    public Instant getInicioEm() {
        return inicioEm;
    }

    public void setInicioEm(Instant inicioEm) {
        this.inicioEm = inicioEm;
    }

    public Instant getFimEm() {
        return fimEm;
    }

    public void setFimEm(Instant fimEm) {
        this.fimEm = fimEm;
    }

    public SessaoEstudoStatus getStatus() {
        return status;
    }

    public void setStatus(SessaoEstudoStatus status) {
        this.status = status;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}