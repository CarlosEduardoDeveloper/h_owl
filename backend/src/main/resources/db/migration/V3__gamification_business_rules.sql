-- Gamificação: streak, biscoitos, floresta, poleiro e seeds de ovo/coruja.

ALTER TABLE usuario
    ADD COLUMN timezone VARCHAR(64) DEFAULT 'America/Fortaleza',
    ADD COLUMN ultimo_estudo_em DATE,
    ADD COLUMN streak_atual INTEGER DEFAULT 0,
    ADD COLUMN melhor_streak INTEGER DEFAULT 0,
    ADD COLUMN ultima_verificacao_diaria DATE;

ALTER TABLE viveiro
    ADD COLUMN saldo_biscoitos INTEGER DEFAULT 0;

ALTER TABLE coruja_usuario
    ADD COLUMN poleiro_indice INTEGER,
    ADD COLUMN dias_sem_biscoito INTEGER DEFAULT 0,
    ADD COLUMN feliz BOOLEAN DEFAULT TRUE;

INSERT INTO tipo_ovo (id, nome, raridade, duracao_minima_minutos, duracao_maxima_minutos, imagem_url, ativo, criado_em)
VALUES
    ('a1000001-0000-4000-8000-000000000010', 'Ovo Rápido', 'COMUM', 10, 10, NULL, TRUE, NOW()),
    ('a1000001-0000-4000-8000-000000000015', 'Ovo Médio', 'COMUM', 15, 15, NULL, TRUE, NOW()),
    ('a1000001-0000-4000-8000-000000000030', 'Ovo Longo', 'COMUM', 30, 30, NULL, TRUE, NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO coruja (id, nome, especie, raridade, descricao, imagem_url, ativo, criado_em)
VALUES
    ('b2000001-0000-4000-8000-000000000001', 'Coruja Sabedoria', 'Tyto alba', 'COMUM', 'Coruja da sabedoria', NULL, TRUE, NOW()),
    ('b2000001-0000-4000-8000-000000000002', 'Coruja Estudiosa', 'Bubo bubo', 'COMUM', 'Amiga dos estudos', NULL, TRUE, NOW()),
    ('b2000001-0000-4000-8000-000000000003', 'Coruja Noturna', 'Strix aluco', 'RARO', 'Guardiã da floresta', NULL, TRUE, NOW())
ON CONFLICT (id) DO NOTHING;
