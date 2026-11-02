package br.com.sistemagestao.service;

import br.com.sistemagestao.dao.FuncionarioDAO;
import br.com.sistemagestao.model.Funcionario;
import br.com.sistemagestao.util.Validacao;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioService() {
        this(new FuncionarioDAO());
    }

    public FuncionarioService(FuncionarioDAO funcionarioDAO) {
        this.funcionarioDAO = funcionarioDAO;
    }

    public List<Funcionario> listarTodos() throws SQLException {
        return funcionarioDAO.listarTodos();
    }

    public Funcionario salvar(Funcionario funcionario) throws SQLException {
        Validacao.validarFuncionario(funcionario.getNome(), funcionario.getDataAdmissao(), funcionario.getSalario());
        funcionario.setNome(funcionario.getNome().trim());
        return funcionario.getUuid() == null ? funcionarioDAO.inserir(funcionario) : atualizar(funcionario);
    }

    public void excluir(UUID uuid) throws SQLException {
        if (uuid == null) {
            throw new IllegalArgumentException("Selecione um funcionário válido.");
        }
        funcionarioDAO.excluir(uuid);
    }

    private Funcionario atualizar(Funcionario funcionario) throws SQLException {
        funcionarioDAO.atualizar(funcionario);
        return funcionario;
    }
}