package br.com.sistemagestao.controller;

import br.com.sistemagestao.model.Usuario;
import br.com.sistemagestao.service.UsuarioService;
import java.sql.SQLException;

public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController() {
        this(new UsuarioService());
    }

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario cadastrar(String nome, String email, char[] senha) throws SQLException {
        return usuarioService.cadastrar(nome, email, senha);
    }
}