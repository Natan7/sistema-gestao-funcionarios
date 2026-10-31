package br.com.sistemagestao.controller;

import br.com.sistemagestao.model.Usuario;
import br.com.sistemagestao.service.UsuarioService;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController() {
        this(new UsuarioService());
    }

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Optional<Usuario> autenticar(String email, char[] senha) throws SQLException {
        return usuarioService.autenticar(email, senha);
    }
}