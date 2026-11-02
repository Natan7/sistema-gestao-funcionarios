package br.com.sistemagestao.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import br.com.sistemagestao.model.Funcionario;
import br.com.sistemagestao.model.Usuario;
import br.com.sistemagestao.service.FuncionarioService;
import br.com.sistemagestao.service.UsuarioService;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ControllersTest {

    @Test
    void loginControllerEncaminhaCredenciaisAoServico() throws SQLException {
        char[] senha = "SenhaSegura123".toCharArray();
        Usuario esperado = usuario();
        UsuarioServiceFake service = new UsuarioServiceFake() {
            @Override
            public Optional<Usuario> autenticar(String email, char[] senhaRecebida) {
                assertEquals("ana@example.com", email);
                assertSame(senha, senhaRecebida);
                return Optional.of(esperado);
            }
        };

        assertEquals(Optional.of(esperado), new LoginController(service).autenticar("ana@example.com", senha));
    }

    @Test
    void usuarioControllerEncaminhaCadastroAoServico() throws SQLException {
        Usuario esperado = usuario();
        UsuarioServiceFake service = new UsuarioServiceFake() {
            @Override
            public Usuario cadastrar(String nome, String email, char[] senha) {
                assertEquals("Ana Souza", nome);
                assertEquals("ana@example.com", email);
                return esperado;
            }
        };

        assertSame(esperado, new UsuarioController(service).cadastrar("Ana Souza", "ana@example.com", "Senha123".toCharArray()));
    }

    @Test
    void funcionarioControllerEncaminhaUuidDoProprietario() throws SQLException {
        UUID usuarioUuid = UUID.randomUUID();
        UUID funcionarioUuid = UUID.randomUUID();
        Funcionario esperado = new Funcionario(funcionarioUuid, "Ana Souza", LocalDate.of(2020, 1, 1), new BigDecimal("2500"), true);
        FuncionarioService service = new FuncionarioService() {
            @Override
            public List<Funcionario> listarTodos(UUID proprietarioUuid) {
                assertEquals(usuarioUuid, proprietarioUuid);
                return List.of(esperado);
            }

            @Override
            public Funcionario salvar(Funcionario funcionario, UUID proprietarioUuid) {
                assertEquals(usuarioUuid, proprietarioUuid);
                return funcionario;
            }

            @Override
            public void excluir(UUID uuid, UUID proprietarioUuid) {
                assertEquals(funcionarioUuid, uuid);
                assertEquals(usuarioUuid, proprietarioUuid);
            }
        };
        FuncionarioController controller = new FuncionarioController(service);

        assertEquals(List.of(esperado), controller.listarTodos(usuarioUuid));
        assertSame(esperado, controller.salvar(esperado, usuarioUuid));
        controller.excluir(funcionarioUuid, usuarioUuid);
    }

    private static Usuario usuario() {
        return new Usuario(UUID.randomUUID(), "Ana Souza", "ana@example.com", "hash", null);
    }

    private static class UsuarioServiceFake extends UsuarioService {
    }
}
