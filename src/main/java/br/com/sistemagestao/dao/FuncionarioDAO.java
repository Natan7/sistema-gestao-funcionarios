package br.com.sistemagestao.dao;

import br.com.sistemagestao.connection.Conexao;
import br.com.sistemagestao.model.Funcionario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FuncionarioDAO {

    public List<Funcionario> listarTodos() throws SQLException {
        String sql = "SELECT uuid, nome, data_admissao, salario, status FROM funcionarios ORDER BY nome";
        List<Funcionario> funcionarios = new ArrayList<>();
        try (Connection conexao = Conexao.obterConexao(); PreparedStatement comando = conexao.prepareStatement(sql); ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                funcionarios.add(new Funcionario(resultado.getObject("uuid", UUID.class), resultado.getString("nome"), resultado.getDate("data_admissao").toLocalDate(), resultado.getBigDecimal("salario"), resultado.getBoolean("status")));
            }
        }
        return funcionarios;
    }

    public Funcionario inserir(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionarios (nome, data_admissao, salario, status) VALUES (?, ?, ?, ?) RETURNING uuid";
        try (Connection conexao = Conexao.obterConexao(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            preencherComando(comando, funcionario);
            try (ResultSet chaves = comando.executeQuery()) {
                if (chaves.next()) {
                    funcionario.setUuid(chaves.getObject("uuid", UUID.class));
                }
            }
            return funcionario;
        }
    }

    public boolean atualizar(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE funcionarios SET nome = ?, data_admissao = ?, salario = ?, status = ? WHERE uuid = ?";
        try (Connection conexao = Conexao.obterConexao(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            preencherComando(comando, funcionario);
            comando.setObject(5, funcionario.getUuid());
            return comando.executeUpdate() == 1;
        }
    }

    public boolean excluir(UUID uuid) throws SQLException {
        try (Connection conexao = Conexao.obterConexao(); PreparedStatement comando = conexao.prepareStatement("DELETE FROM funcionarios WHERE uuid = ?")) {
            comando.setObject(1, uuid);
            return comando.executeUpdate() == 1;
        }
    }

    private void preencherComando(PreparedStatement comando, Funcionario funcionario) throws SQLException {
        comando.setString(1, funcionario.getNome());
        comando.setDate(2, Date.valueOf(funcionario.getDataAdmissao()));
        comando.setBigDecimal(3, funcionario.getSalario());
        comando.setBoolean(4, funcionario.isStatus());
    }
}