package br.com.sistemagestao.connection;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Conexao {

    private static final String URL_PADRAO = "jdbc:postgresql://localhost:5432/sistema_gestao_funcionarios";

    private Conexao() {
    }

    public static Connection obterConexao() throws SQLException {
        Properties arquivo = carregarConfiguracao();
        String url = obterConfiguracao("DB_URL", "db.url", URL_PADRAO, arquivo);
        String usuario = obterConfiguracao("DB_USER", "db.user", "postgres", arquivo);
        String senha = obterConfiguracao("DB_PASSWORD", "db.password", "", arquivo);
        try {
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException exception) {
            throw new SQLException("Não foi possível conectar ao PostgreSQL. Verifique o serviço e as configurações DB_URL, DB_USER e DB_PASSWORD.", exception);
        }
    }

    public static void migrarBanco() throws SQLException {
        Properties arquivo = carregarConfiguracao();
        String url = obterConfiguracao("DB_URL", "db.url", URL_PADRAO, arquivo);
        String usuario = obterConfiguracao("DB_USER", "db.user", "postgres", arquivo);
        String senha = obterConfiguracao("DB_PASSWORD", "db.password", "", arquivo);
        try {
            Flyway.configure()
                    .dataSource(url, usuario, senha)
                    .locations("classpath:db/migration")
                    .load()
                    .migrate();
        } catch (FlywayException exception) {
            throw new SQLException("Não foi possível aplicar as migrações do banco de dados.", exception);
        }
    }

    private static String obterConfiguracao(String variavel, String propriedade, String padrao, Properties arquivo) {
        String valor = System.getProperty(propriedade);
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(variavel);
        }
        if (valor == null || valor.isBlank()) {
            valor = arquivo.getProperty(propriedade);
        }
        return valor == null ? padrao : valor;
    }

    private static Properties carregarConfiguracao() throws SQLException {

        Properties propriedades = new Properties();

        try (InputStream entrada = Conexao.class.getClassLoader()
                .getResourceAsStream("config/application-local.properties")) {

            if (entrada == null) {
                throw new SQLException(
                        "Arquivo config/application-local.properties não encontrado."
                );
            }

            propriedades.load(entrada);

        } catch (IOException exception) {
            throw new SQLException(
                    "Erro ao carregar configurações do banco.",
                    exception
            );
        }

        return propriedades;
    }
}