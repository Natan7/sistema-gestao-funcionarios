# Sistema de Gestao de Funcionarios

Aplicacao desktop Java Swing com FlatLaf, PostgreSQL, JDBC e arquitetura MVC + DAO.

## Requisitos

- JDK 21
- Apache Maven 3.9 ou superior
- PostgreSQL 14 ou superior

## Banco de dados

1. Execute `src/main/resources/sql/database.sql` conectado ao banco administrativo do PostgreSQL.
2. Configure a conexao por variaveis de ambiente ou pelo arquivo externo `%USERPROFILE%\.sistema-gestao-funcionarios.properties` (modelo em `src/main/resources/config/application-local.properties`).
3. Inicie a aplicacao. O Flyway cria e atualiza as tabelas automaticamente antes de abrir o login.

As migracoes ficam em `src/main/resources/db/migration` e sao registradas na tabela `flyway_schema_history`. A V1 cria usuarios e funcionarios com UUID como chave primaria; a V2 associa cada funcionario ao usuario proprietario. Em bancos com funcionarios anteriores a V2, o proprietario nao pode ser inferido: esses registros ficam sem acesso pela aplicacao ate que sejam atribuidos manualmente ao usuario correto.

Para revisar registros antigos sem proprietario, consulte-os e atribua-os somente depois de confirmar a conta correta:

```sql
SELECT uuid, nome FROM funcionarios WHERE usuario_uuid IS NULL;

UPDATE funcionarios AS f
SET usuario_uuid = u.uuid
FROM usuarios AS u
WHERE u.email = 'email-do-proprietario@exemplo.com'
	AND f.usuario_uuid IS NULL;
```

Para reinicializar um banco de desenvolvimento e aplicar a V1 desde o inicio, apague as tabelas antigas e o historico anterior antes de iniciar a aplicacao. Esta operacao apaga todos os dados:

```sql
DROP TABLE IF EXISTS usuarios, funcionarios, flyway_schema_history CASCADE;
```

Depois, inicie a aplicacao para o Flyway criar as tabelas e registrar novamente a V1. Nao execute `schema.sql` manualmente em um banco gerenciado pelo Flyway.

Variaveis aceitas: `DB_URL`, `DB_USER`, `DB_PASSWORD` e, opcionalmente, `DB_CONFIG_FILE`. As propriedades de sistema `db.url`, `db.user`, `db.password` e `db.config` tambem sao aceitas. O padrao e `localhost:5432`, banco `sistema_gestao_funcionarios` e usuario `postgres`; nenhuma senha real e incluida no codigo.

No PowerShell, por exemplo:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/sistema_gestao_funcionarios"
$env:DB_USER = "postgres"
$env:DB_PASSWORD = "sua-senha-local"
```

## Executar

Abra a pasta do projeto no Apache NetBeans e execute a classe `br.com.sistemagestao.App`, ou use:

```powershell
mvn clean package
mvn exec:java
```

O primeiro usuario deve ser criado em **Criar conta**. O sistema usa SHA-256 hexadecimal conforme solicitado. SHA-256 simples nao e recomendado para armazenamento de senhas em producao; nesse caso, prefira Argon2id ou BCrypt.

## Organizacao

- `connection`: conexao JDBC e configuracao
- `model`: entidades Usuario e Funcionario
- `dao`: comandos SQL preparados
- `service`: validacoes e regras de negocio
- `controller`: ponte entre telas e servicos
- `view`: telas Swing
- `util`: criptografia, validacao e formatacao
- `src/main/resources/db/migration`: historico versionado de alteracoes no schema
- `src/main/resources/sql`: criacao do banco e scripts de referencia