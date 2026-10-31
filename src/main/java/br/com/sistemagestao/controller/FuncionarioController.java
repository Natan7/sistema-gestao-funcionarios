package br.com.sistemagestao.controller;

import br.com.sistemagestao.model.Funcionario;
import br.com.sistemagestao.service.FuncionarioService;
import java.sql.SQLException;
import java.util.List;

public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController() {
        this(new FuncionarioService());
    }

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    public List<Funcionario> listarTodos() throws SQLException {
        return funcionarioService.listarTodos();
    }

    public Funcionario salvar(Funcionario funcionario) throws SQLException {
        return funcionarioService.salvar(funcionario);
    }

    public void excluir(int id) throws SQLException {
        funcionarioService.excluir(id);
    }
}