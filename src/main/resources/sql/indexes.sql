CREATE UNIQUE INDEX IF NOT EXISTS idx_usuarios_email_lower
    ON usuarios (lower(email));

CREATE INDEX IF NOT EXISTS idx_funcionarios_nome
    ON funcionarios (nome);

CREATE INDEX IF NOT EXISTS idx_funcionarios_data_admissao
    ON funcionarios (data_admissao);