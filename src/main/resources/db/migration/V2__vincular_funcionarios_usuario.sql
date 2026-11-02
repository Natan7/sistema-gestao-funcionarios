DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'funcionarios'
          AND column_name = 'usuario_uuid'
    ) THEN
        ALTER TABLE funcionarios
            ADD COLUMN usuario_uuid UUID REFERENCES usuarios(uuid) ON DELETE CASCADE;
    END IF;

    CREATE INDEX IF NOT EXISTS idx_funcionarios_usuario_uuid
        ON funcionarios (usuario_uuid);

    IF NOT EXISTS (SELECT 1 FROM funcionarios WHERE usuario_uuid IS NULL) THEN
        ALTER TABLE funcionarios ALTER COLUMN usuario_uuid SET NOT NULL;
    END IF;
END $$;