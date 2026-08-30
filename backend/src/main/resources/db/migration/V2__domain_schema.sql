-- Domain schema: all business tables with soft-delete columns and nullable FKs.

CREATE TABLE pessoa (
    id              UUID PRIMARY KEY,
    nome            VARCHAR(255),
    data_nascimento DATE,
    genero          VARCHAR(255),
    foto_url        VARCHAR(2048),
    ativo           BOOLEAN,
    criado_em       TIMESTAMPTZ NOT NULL,
    atualizado_em   TIMESTAMPTZ,
    excluido_em     TIMESTAMPTZ
);

CREATE TABLE usuario (
    id              UUID PRIMARY KEY,
    pessoa_id       UUID,
    email           VARCHAR(320),
    senha_hash      VARCHAR(255),
    status          VARCHAR(50),
    ativo           BOOLEAN,
    criado_em       TIMESTAMPTZ NOT NULL,
    atualizado_em   TIMESTAMPTZ,
    excluido_em     TIMESTAMPTZ,
    CONSTRAINT fk_usuario_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa (id)
);

CREATE INDEX idx_usuario_email ON usuario (email);

CREATE TABLE preferencia_usuario (
    id                      UUID PRIMARY KEY,
    usuario_id              UUID,
    tema                    VARCHAR(50),
    modo_foco_padrao        VARCHAR(50),
    notificacoes_ativas     BOOLEAN,
    duracao_foco_padrao     INTEGER,
    versao_biblia_preferida VARCHAR(255),
    ativo                   BOOLEAN,
    criado_em               TIMESTAMPTZ NOT NULL,
    atualizado_em           TIMESTAMPTZ,
    excluido_em             TIMESTAMPTZ,
    CONSTRAINT fk_preferencia_usuario_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE TABLE trilha (
    id                  UUID PRIMARY KEY,
    titulo              VARCHAR(255),
    descricao           TEXT,
    imagem_url          VARCHAR(2048),
    nivel_dificuldade   VARCHAR(50),
    ordem               INTEGER,
    ativo               BOOLEAN,
    criado_em           TIMESTAMPTZ NOT NULL,
    atualizado_em       TIMESTAMPTZ,
    excluido_em         TIMESTAMPTZ
);

CREATE TABLE modulo (
    id                      UUID PRIMARY KEY,
    trilha_id               UUID,
    titulo                  VARCHAR(255),
    descricao               TEXT,
    ordem                   INTEGER,
    tempo_sugerido_minutos  INTEGER,
    ativo                   BOOLEAN,
    criado_em               TIMESTAMPTZ NOT NULL,
    atualizado_em           TIMESTAMPTZ,
    excluido_em             TIMESTAMPTZ,
    CONSTRAINT fk_modulo_trilha FOREIGN KEY (trilha_id) REFERENCES trilha (id)
);

CREATE INDEX idx_modulo_trilha_id ON modulo (trilha_id);

CREATE TABLE conteudo (
    id              UUID PRIMARY KEY,
    modulo_id       UUID,
    tipo            VARCHAR(50),
    titulo          VARCHAR(255),
    conteudo        TEXT,
    url             VARCHAR(2048),
    ordem           INTEGER,
    ativo           BOOLEAN,
    criado_em       TIMESTAMPTZ NOT NULL,
    atualizado_em   TIMESTAMPTZ,
    excluido_em     TIMESTAMPTZ,
    CONSTRAINT fk_conteudo_modulo FOREIGN KEY (modulo_id) REFERENCES modulo (id)
);

CREATE INDEX idx_conteudo_modulo_id ON conteudo (modulo_id);

CREATE TABLE sessao_estudo (
    id                          UUID PRIMARY KEY,
    usuario_id                  UUID,
    intencao                    VARCHAR(50),
    modo_foco                   VARCHAR(50),
    duracao_planejada_minutos   INTEGER,
    duracao_real_minutos        INTEGER,
    inicio_em                   TIMESTAMPTZ,
    fim_em                      TIMESTAMPTZ,
    status                      VARCHAR(50),
    ativo                       BOOLEAN,
    criado_em                   TIMESTAMPTZ NOT NULL,
    atualizado_em               TIMESTAMPTZ,
    excluido_em                 TIMESTAMPTZ,
    CONSTRAINT fk_sessao_estudo_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE INDEX idx_sessao_estudo_usuario_id ON sessao_estudo (usuario_id);
CREATE INDEX idx_sessao_estudo_status ON sessao_estudo (status);

CREATE TABLE conteudo_sessao (
    id                  UUID PRIMARY KEY,
    sessao_estudo_id    UUID,
    tipo                VARCHAR(50),
    referencia_id       UUID,
    titulo              VARCHAR(255),
    descricao           TEXT,
    ordem               INTEGER,
    ativo               BOOLEAN,
    criado_em           TIMESTAMPTZ NOT NULL,
    atualizado_em       TIMESTAMPTZ,
    excluido_em         TIMESTAMPTZ,
    CONSTRAINT fk_conteudo_sessao_sessao_estudo FOREIGN KEY (sessao_estudo_id) REFERENCES sessao_estudo (id)
);

CREATE TABLE progresso_trilha (
    id                      UUID PRIMARY KEY,
    usuario_id              UUID,
    trilha_id               UUID,
    modulo_atual_id         UUID,
    status                  VARCHAR(50),
    progresso_percentual    INTEGER,
    ultimo_acesso_em        TIMESTAMPTZ,
    concluido_em            TIMESTAMPTZ,
    ativo                   BOOLEAN,
    criado_em               TIMESTAMPTZ NOT NULL,
    atualizado_em           TIMESTAMPTZ,
    excluido_em             TIMESTAMPTZ,
    CONSTRAINT fk_progresso_trilha_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_progresso_trilha_trilha FOREIGN KEY (trilha_id) REFERENCES trilha (id),
    CONSTRAINT fk_progresso_trilha_modulo_atual FOREIGN KEY (modulo_atual_id) REFERENCES modulo (id)
);

CREATE INDEX idx_progresso_trilha_usuario_id ON progresso_trilha (usuario_id);
CREATE INDEX idx_progresso_trilha_trilha_id ON progresso_trilha (trilha_id);

CREATE TABLE quiz (
    id              UUID PRIMARY KEY,
    modulo_id       UUID,
    titulo          VARCHAR(255),
    descricao       TEXT,
    ordem           INTEGER,
    ativo           BOOLEAN,
    criado_em       TIMESTAMPTZ NOT NULL,
    atualizado_em   TIMESTAMPTZ,
    excluido_em     TIMESTAMPTZ,
    CONSTRAINT fk_quiz_modulo FOREIGN KEY (modulo_id) REFERENCES modulo (id)
);

CREATE TABLE questao (
    id              UUID PRIMARY KEY,
    quiz_id         UUID,
    enunciado       TEXT,
    ordem           INTEGER,
    ativo           BOOLEAN,
    criado_em       TIMESTAMPTZ NOT NULL,
    atualizado_em   TIMESTAMPTZ,
    excluido_em     TIMESTAMPTZ,
    CONSTRAINT fk_questao_quiz FOREIGN KEY (quiz_id) REFERENCES quiz (id)
);

CREATE TABLE alternativa (
    id              UUID PRIMARY KEY,
    questao_id      UUID,
    texto           TEXT,
    correta         BOOLEAN,
    ordem           INTEGER,
    ativo           BOOLEAN,
    criado_em       TIMESTAMPTZ NOT NULL,
    atualizado_em   TIMESTAMPTZ,
    excluido_em     TIMESTAMPTZ,
    CONSTRAINT fk_alternativa_questao FOREIGN KEY (questao_id) REFERENCES questao (id)
);

CREATE TABLE tentativa_quiz (
    id                  UUID PRIMARY KEY,
    usuario_id          UUID,
    quiz_id             UUID,
    sessao_estudo_id    UUID,
    pontuacao           INTEGER,
    acertos             INTEGER,
    total_questoes      INTEGER,
    realizado_em        TIMESTAMPTZ,
    ativo               BOOLEAN,
    criado_em           TIMESTAMPTZ NOT NULL,
    atualizado_em       TIMESTAMPTZ,
    excluido_em         TIMESTAMPTZ,
    CONSTRAINT fk_tentativa_quiz_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_tentativa_quiz_quiz FOREIGN KEY (quiz_id) REFERENCES quiz (id),
    CONSTRAINT fk_tentativa_quiz_sessao_estudo FOREIGN KEY (sessao_estudo_id) REFERENCES sessao_estudo (id)
);

CREATE INDEX idx_tentativa_quiz_usuario_id ON tentativa_quiz (usuario_id);

CREATE TABLE resposta_questao (
    id                  UUID PRIMARY KEY,
    tentativa_quiz_id   UUID,
    questao_id          UUID,
    alternativa_id      UUID,
    correta             BOOLEAN,
    ativo               BOOLEAN,
    criado_em           TIMESTAMPTZ NOT NULL,
    atualizado_em       TIMESTAMPTZ,
    excluido_em         TIMESTAMPTZ,
    CONSTRAINT fk_resposta_questao_tentativa_quiz FOREIGN KEY (tentativa_quiz_id) REFERENCES tentativa_quiz (id),
    CONSTRAINT fk_resposta_questao_questao FOREIGN KEY (questao_id) REFERENCES questao (id),
    CONSTRAINT fk_resposta_questao_alternativa FOREIGN KEY (alternativa_id) REFERENCES alternativa (id)
);

CREATE TABLE tipo_ovo (
    id                      UUID PRIMARY KEY,
    nome                    VARCHAR(255),
    raridade                VARCHAR(50),
    duracao_minima_minutos  INTEGER,
    duracao_maxima_minutos  INTEGER,
    imagem_url              VARCHAR(2048),
    ativo                   BOOLEAN,
    criado_em               TIMESTAMPTZ NOT NULL,
    atualizado_em           TIMESTAMPTZ,
    excluido_em             TIMESTAMPTZ
);

CREATE TABLE coruja (
    id              UUID PRIMARY KEY,
    nome            VARCHAR(255),
    especie         VARCHAR(255),
    raridade        VARCHAR(50),
    descricao       TEXT,
    imagem_url      VARCHAR(2048),
    ativo           BOOLEAN,
    criado_em       TIMESTAMPTZ NOT NULL,
    atualizado_em   TIMESTAMPTZ,
    excluido_em     TIMESTAMPTZ
);

CREATE TABLE conquista (
    id              UUID PRIMARY KEY,
    nome            VARCHAR(255),
    descricao       TEXT,
    icone_url       VARCHAR(2048),
    categoria       VARCHAR(255),
    regra           TEXT,
    ativo           BOOLEAN,
    criado_em       TIMESTAMPTZ NOT NULL,
    atualizado_em   TIMESTAMPTZ,
    excluido_em     TIMESTAMPTZ
);

CREATE TABLE ovo_usuario (
    id                  UUID PRIMARY KEY,
    usuario_id          UUID,
    tipo_ovo_id         UUID,
    sessao_estudo_id    UUID,
    status              VARCHAR(50),
    chocado_em          TIMESTAMPTZ,
    ativo               BOOLEAN,
    criado_em           TIMESTAMPTZ NOT NULL,
    atualizado_em       TIMESTAMPTZ,
    excluido_em         TIMESTAMPTZ,
    CONSTRAINT fk_ovo_usuario_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_ovo_usuario_tipo_ovo FOREIGN KEY (tipo_ovo_id) REFERENCES tipo_ovo (id),
    CONSTRAINT fk_ovo_usuario_sessao_estudo FOREIGN KEY (sessao_estudo_id) REFERENCES sessao_estudo (id)
);

CREATE INDEX idx_ovo_usuario_usuario_id ON ovo_usuario (usuario_id);

CREATE TABLE coruja_usuario (
    id                  UUID PRIMARY KEY,
    usuario_id          UUID,
    coruja_id           UUID,
    sessao_estudo_id    UUID,
    adquirida_em        TIMESTAMPTZ,
    nivel               INTEGER,
    experiencia         INTEGER,
    observacoes         TEXT,
    ativo               BOOLEAN,
    criado_em           TIMESTAMPTZ NOT NULL,
    atualizado_em       TIMESTAMPTZ,
    excluido_em         TIMESTAMPTZ,
    CONSTRAINT fk_coruja_usuario_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_coruja_usuario_coruja FOREIGN KEY (coruja_id) REFERENCES coruja (id),
    CONSTRAINT fk_coruja_usuario_sessao_estudo FOREIGN KEY (sessao_estudo_id) REFERENCES sessao_estudo (id)
);

CREATE INDEX idx_coruja_usuario_usuario_id ON coruja_usuario (usuario_id);

CREATE TABLE recompensa (
    id                  UUID PRIMARY KEY,
    usuario_id          UUID,
    coruja_id           UUID,
    sessao_estudo_id    UUID,
    ovo_usuario_id      UUID,
    tipo                VARCHAR(50),
    titulo              VARCHAR(255),
    descricao           TEXT,
    concedida_em        TIMESTAMPTZ,
    ativo               BOOLEAN,
    criado_em           TIMESTAMPTZ NOT NULL,
    atualizado_em       TIMESTAMPTZ,
    excluido_em         TIMESTAMPTZ,
    CONSTRAINT fk_recompensa_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_recompensa_coruja FOREIGN KEY (coruja_id) REFERENCES coruja (id),
    CONSTRAINT fk_recompensa_sessao_estudo FOREIGN KEY (sessao_estudo_id) REFERENCES sessao_estudo (id),
    CONSTRAINT fk_recompensa_ovo_usuario FOREIGN KEY (ovo_usuario_id) REFERENCES ovo_usuario (id)
);

CREATE INDEX idx_recompensa_usuario_id ON recompensa (usuario_id);
CREATE INDEX idx_recompensa_coruja_id ON recompensa (coruja_id);
CREATE INDEX idx_recompensa_criado_em ON recompensa (criado_em);

CREATE TABLE viveiro (
    id              UUID PRIMARY KEY,
    usuario_id      UUID,
    nome            VARCHAR(255),
    nivel           INTEGER,
    xp_total        BIGINT,
    tema_visual     VARCHAR(255),
    ativo           BOOLEAN,
    criado_em       TIMESTAMPTZ NOT NULL,
    atualizado_em   TIMESTAMPTZ,
    excluido_em     TIMESTAMPTZ,
    CONSTRAINT fk_viveiro_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE INDEX idx_viveiro_usuario_id ON viveiro (usuario_id);

CREATE TABLE decoracao_viveiro (
    id              UUID PRIMARY KEY,
    viveiro_id      UUID,
    nome            VARCHAR(255),
    tipo            VARCHAR(50),
    imagem_url      VARCHAR(2048),
    adquirida_em    TIMESTAMPTZ,
    ativo           BOOLEAN,
    criado_em       TIMESTAMPTZ NOT NULL,
    atualizado_em   TIMESTAMPTZ,
    excluido_em     TIMESTAMPTZ,
    CONSTRAINT fk_decoracao_viveiro_viveiro FOREIGN KEY (viveiro_id) REFERENCES viveiro (id)
);

CREATE TABLE nota (
    id                  UUID PRIMARY KEY,
    usuario_id          UUID,
    sessao_estudo_id    UUID,
    titulo              VARCHAR(255),
    conteudo            TEXT,
    ativo               BOOLEAN,
    criado_em           TIMESTAMPTZ NOT NULL,
    atualizado_em       TIMESTAMPTZ,
    excluido_em         TIMESTAMPTZ,
    CONSTRAINT fk_nota_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_nota_sessao_estudo FOREIGN KEY (sessao_estudo_id) REFERENCES sessao_estudo (id)
);

CREATE INDEX idx_nota_usuario_id ON nota (usuario_id);

CREATE TABLE destaque (
    id                  UUID PRIMARY KEY,
    usuario_id          UUID,
    sessao_estudo_id    UUID,
    tipo                VARCHAR(50),
    referencia_id       UUID,
    texto               TEXT,
    cor                 VARCHAR(50),
    ativo               BOOLEAN,
    criado_em           TIMESTAMPTZ NOT NULL,
    atualizado_em       TIMESTAMPTZ,
    excluido_em         TIMESTAMPTZ,
    CONSTRAINT fk_destaque_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_destaque_sessao_estudo FOREIGN KEY (sessao_estudo_id) REFERENCES sessao_estudo (id)
);

CREATE TABLE consulta_sabio (
    id                  UUID PRIMARY KEY,
    usuario_id          UUID,
    sessao_estudo_id    UUID,
    pergunta            TEXT,
    resposta            TEXT,
    contexto_referencia VARCHAR(512),
    ativo               BOOLEAN,
    criado_em           TIMESTAMPTZ NOT NULL,
    atualizado_em       TIMESTAMPTZ,
    excluido_em         TIMESTAMPTZ,
    CONSTRAINT fk_consulta_sabio_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_consulta_sabio_sessao_estudo FOREIGN KEY (sessao_estudo_id) REFERENCES sessao_estudo (id)
);

CREATE INDEX idx_consulta_sabio_usuario_id ON consulta_sabio (usuario_id);

CREATE TABLE item_revisao (
    id                  UUID PRIMARY KEY,
    usuario_id          UUID,
    tipo                VARCHAR(50),
    referencia_id       UUID,
    proxima_revisao_em  TIMESTAMPTZ,
    intervalo_dias      INTEGER,
    facilidade          INTEGER,
    repeticoes          INTEGER,
    ativo               BOOLEAN,
    criado_em           TIMESTAMPTZ NOT NULL,
    atualizado_em       TIMESTAMPTZ,
    excluido_em         TIMESTAMPTZ,
    CONSTRAINT fk_item_revisao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE INDEX idx_item_revisao_usuario_id ON item_revisao (usuario_id);

CREATE TABLE conquista_usuario (
    id              UUID PRIMARY KEY,
    usuario_id      UUID,
    conquista_id    UUID,
    conquistada_em  TIMESTAMPTZ,
    progresso       INTEGER,
    ativo           BOOLEAN,
    criado_em       TIMESTAMPTZ NOT NULL,
    atualizado_em   TIMESTAMPTZ,
    excluido_em     TIMESTAMPTZ,
    CONSTRAINT fk_conquista_usuario_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_conquista_usuario_conquista FOREIGN KEY (conquista_id) REFERENCES conquista (id)
);
