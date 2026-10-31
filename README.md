# Sistema de Gestao de Funcionarios

Aplicacao desktop Java Swing com FlatLaf, PostgreSQL, JDBC e arquitetura MVC + DAO.

## Requisitos

- JDK 21
- Apache Maven 3.9 ou superior
- PostgreSQL 14 ou superior

## Banco de dados

1. Execute `src/main/resources/sql/database.sql` conectado ao banco administrativo do PostgreSQL.
2. Conecte-se ao banco `sistema_gestao_funcionarios` e execute `schema.sql` e `indexes.sql`.
3. Configure a conexao por variaveis de ambiente ou pelo arquivo externo `%USERPROFILE%\.sistema-gestao-funcionarios.properties` (modelo em `src/main/resources/config/application-local.properties`).

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
- `src/main/resources/sql`: criacao do banco, tabelas e indices