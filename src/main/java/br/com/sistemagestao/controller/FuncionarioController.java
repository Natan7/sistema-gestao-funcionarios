package br.com.sistemagestao.controller;

import br.com.sistemagestao.model.Funcionario;
import br.com.sistemagestao.service.FuncionarioService;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController() {
        this(new FuncionarioService());
    }

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    public List<Funcionario> listarTodos(UUID usuarioUuid) throws SQLException {
        return funcionarioService.listarTodos(usuarioUuid);
    }

    public Funcionario salvar(Funcionario funcionario, UUID usuarioUuid) throws SQLException {
        return funcionarioService.salvar(funcionario, usuarioUuid);
    }

    public void excluir(UUID uuid, UUID usuarioUuid) throws SQLException {
        funcionarioService.excluir(uuid, usuarioUuid);
    }
}