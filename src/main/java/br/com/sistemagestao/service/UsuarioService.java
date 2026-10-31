package br.com.sistemagestao.service;

import br.com.sistemagestao.dao.UsuarioDAO;
import br.com.sistemagestao.model.Usuario;
import br.com.sistemagestao.util.Criptografia;
import br.com.sistemagestao.util.Validacao;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this(new UsuarioDAO());
    }

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Optional<Usuario> autenticar(String email, char[] senha) throws SQLException {
        String emailNormalizado = email == null ? "" : email.trim();
        if (emailNormalizado.isEmpty() || senha == null || senha.length == 0 || !Validacao.emailValido(emailNormalizado)) {
            return Optional.empty();
        }
        Optional<Usuario> usuario = usuarioDAO.buscarPorEmail(emailNormalizado);
        if (usuario.isPresent() && Criptografia.comparar(Criptografia.sha256(senha), usuario.get().getSenha())) {
            return usuario;
        }
        return Optional.empty();
    }

    public Usuario cadastrar(String nome, String email, char[] senha) throws SQLException {
        String nomeNormalizado = nome == null ? "" : nome.trim();
        String emailNormalizado = email == null ? "" : email.trim();
        if (nomeNormalizado.length() < 2) {
            throw new IllegalArgumentException("Informe seu nome completo com pelo menos 2 caracteres.");
        }
        if (!Validacao.emailValido(emailNormalizado)) {
            throw new IllegalArgumentException("Informe um e-mail válido.");
        }
        if (senha == null || senha.length < 8) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");
        }
        if (usuarioDAO.emailExiste(emailNormalizado)) {
            throw new SQLIntegrityConstraintViolationException("Este e-mail já está cadastrado.", "23505");
        }
        try {
            return usuarioDAO.inserir(new Usuario(0, nomeNormalizado, emailNormalizado, Criptografia.sha256(senha), null));
        } catch (SQLException exception) {
            if ("23505".equals(exception.getSQLState())) {
                throw new SQLIntegrityConstraintViolationException("Este e-mail já está cadastrado.", "23505", exception);
            }
            throw exception;
        }
    }
}