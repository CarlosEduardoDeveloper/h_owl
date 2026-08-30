package com.example.foundation.modules.quiz.domain;

import com.example.foundation.modules.quiz.domain.Alternativa;
import com.example.foundation.modules.quiz.domain.Questao;
import com.example.foundation.modules.quiz.domain.TentativaQuiz;
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
@Table(name = "resposta_questao")
public class RespostaQuestao extends BaseEntity {

    @Column(name = "correta")
    private Boolean correta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tentativa_quiz_id")
    private TentativaQuiz tentativaQuiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questao_id")
    private Questao questao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternativa_id")
    private Alternativa alternativa;


    public Boolean getCorreta() {
        return correta;
    }

    public void setCorreta(Boolean correta) {
        this.correta = correta;
    }

    public TentativaQuiz getTentativaQuiz() {
        return tentativaQuiz;
    }

    public void setTentativaQuiz(TentativaQuiz tentativaQuiz) {
        this.tentativaQuiz = tentativaQuiz;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public Alternativa getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(Alternativa alternativa) {
        this.alternativa = alternativa;
    }
}