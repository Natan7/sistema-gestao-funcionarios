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

    public List<Funcionario> listarTodos(UUID usuarioUuid) throws SQLException {
        validarUsuario(usuarioUuid);
        return funcionarioDAO.listarTodos(usuarioUuid);
    }

    public Funcionario salvar(Funcionario funcionario, UUID usuarioUuid) throws SQLException {
        validarUsuario(usuarioUuid);
        Validacao.validarFuncionario(funcionario.getNome(), funcionario.getDataAdmissao(), funcionario.getSalario());
        funcionario.setNome(funcionario.getNome().trim());
        return funcionario.getUuid() == null ? funcionarioDAO.inserir(funcionario, usuarioUuid) : atualizar(funcionario, usuarioUuid);
    }

    public void excluir(UUID uuid, UUID usuarioUuid) throws SQLException {
        validarUsuario(usuarioUuid);
        if (uuid == null) {
            throw new IllegalArgumentException("Selecione um funcionário válido.");
        }
        if (!funcionarioDAO.excluir(uuid, usuarioUuid)) {
            throw new IllegalArgumentException("O funcionário não existe ou não pertence a este usuário.");
        }
    }

    private Funcionario atualizar(Funcionario funcionario, UUID usuarioUuid) throws SQLException {
        if (!funcionarioDAO.atualizar(funcionario, usuarioUuid)) {
            throw new IllegalArgumentException("O funcionário não existe ou não pertence a este usuário.");
        }
        return funcionario;
    }

    private static void validarUsuario(UUID usuarioUuid) {
        if (usuarioUuid == null) {
            throw new IllegalArgumentException("Usuário autenticado inválido.");
        }
    }
}