package br.com.sistemagestao.dao;

import br.com.sistemagestao.connection.Conexao;
import br.com.sistemagestao.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public class UsuarioDAO {

    public Optional<Usuario> buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT uuid, nome, email, senha, data_cadastro FROM usuarios WHERE lower(email) = lower(?)";
        try (Connection conexao = Conexao.obterConexao(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, email);
            try (ResultSet resultado = comando.executeQuery()) {
                if (!resultado.next()) {
                    return Optional.empty();
                }
                Timestamp cadastro = resultado.getTimestamp("data_cadastro");
                return Optional.of(new Usuario(resultado.getObject("uuid", UUID.class), resultado.getString("nome"), resultado.getString("email"), resultado.getString("senha"), cadastro.toLocalDateTime()));
            }
        }
    }

    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE lower(email) = lower(?)";
        try (Connection conexao = Conexao.obterConexao(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, email);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next();
            }
        }
    }

    public Usuario inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?) RETURNING uuid";
        try (Connection conexao = Conexao.obterConexao(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getEmail());
            comando.setString(3, usuario.getSenha());
            try (ResultSet chaves = comando.executeQuery()) {
                if (chaves.next()) {
                    usuario.setUuid(chaves.getObject("uuid", UUID.class));
                }
            }
            return usuario;
        }
    }
}