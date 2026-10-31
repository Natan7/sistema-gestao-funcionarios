package br.com.sistemagestao.dao;

import br.com.sistemagestao.connection.Conexao;
import br.com.sistemagestao.model.Funcionario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public List<Funcionario> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, data_admissao, salario, status FROM funcionarios ORDER BY nome";
        List<Funcionario> funcionarios = new ArrayList<>();
        try (Connection conexao = Conexao.obterConexao(); PreparedStatement comando = conexao.prepareStatement(sql); ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                funcionarios.add(new Funcionario(resultado.getInt("id"), resultado.getString("nome"), resultado.getDate("data_admissao").toLocalDate(), resultado.getBigDecimal("salario"), resultado.getBoolean("status")));
            }
        }
        return funcionarios;
    }

    public Funcionario inserir(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionarios (nome, data_admissao, salario, status) VALUES (?, ?, ?, ?)";
        try (Connection conexao = Conexao.obterConexao(); PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherComando(comando, funcionario);
            comando.executeUpdate();
            try (ResultSet chaves = comando.getGeneratedKeys()) {
                if (chaves.next()) {
                    funcionario.setId(chaves.getInt(1));
                }
            }
            return funcionario;
        }
    }

    public boolean atualizar(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE funcionarios SET nome = ?, data_admissao = ?, salario = ?, status = ? WHERE id = ?";
        try (Connection conexao = Conexao.obterConexao(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            preencherComando(comando, funcionario);
            comando.setInt(5, funcionario.getId());
            return comando.executeUpdate() == 1;
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conexao = Conexao.obterConexao(); PreparedStatement comando = conexao.prepareStatement("DELETE FROM funcionarios WHERE id = ?")) {
            comando.setInt(1, id);
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