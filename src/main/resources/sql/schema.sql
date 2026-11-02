CREATE TABLE IF NOT EXISTS usuarios (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(64) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS funcionarios (
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_uuid UUID NOT NULL REFERENCES usuarios(uuid) ON DELETE CASCADE,
    nome VARCHAR(150) NOT NULL,
    data_admissao DATE NOT NULL,
    salario NUMERIC(12, 2) NOT NULL CHECK (salario > 0),
    status BOOLEAN NOT NULL DEFAULT TRUE
);