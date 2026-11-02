package br.com.sistemagestao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import br.com.sistemagestao.dao.FuncionarioDAO;
import br.com.sistemagestao.model.Funcionario;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FuncionarioServiceTest {

    private final UUID usuarioA = UUID.randomUUID();
    private final UUID usuarioB = UUID.randomUUID();
    private InMemoryFuncionarioDAO dao;
    private FuncionarioService service;

    @BeforeEach
    void configurar() {
        dao = new InMemoryFuncionarioDAO();
        service = new FuncionarioService(dao);
    }

    @Test
    void criaFuncionarioAssociadoAoUsuarioENormalizaNome() throws SQLException {
        Funcionario funcionario = novoFuncionario(null, "  Ana Souza  ");

        Funcionario salvo = service.salvar(funcionario, usuarioA);

        assertEquals("Ana Souza", salvo.getNome());
        assertTrue(salvo.getUuid() != null);
        assertEquals(salvo.getUuid(), service.listarTodos(usuarioA).get(0).getUuid());
        assertTrue(service.listarTodos(usuarioB).isEmpty());
    }

    @Test
    void listaSomenteFuncionariosDoProprietario() throws SQLException {
        Funcionario funcionarioA = service.salvar(novoFuncionario(null, "Ana"), usuarioA);
        Funcionario funcionarioB = service.salvar(novoFuncionario(null, "Bia"), usuarioB);

        assertEquals(List.of(funcionarioA.getUuid()), service.listarTodos(usuarioA).stream().map(Funcionario::getUuid).toList());
        assertEquals(List.of(funcionarioB.getUuid()), service.listarTodos(usuarioB).stream().map(Funcionario::getUuid).toList());
    }

    @Test
    void atualizaSomenteFuncionarioDoProprietario() throws SQLException {
        Funcionario funcionario = service.salvar(novoFuncionario(null, "Ana Souza"), usuarioA);
        funcionario.setNome("Ana Silva");

        assertEquals(funcionario, service.salvar(funcionario, usuarioA));
        funcionario.setNome("Tentativa cruzada");
        assertThrows(IllegalArgumentException.class, () -> service.salvar(funcionario, usuarioB));
        assertEquals("Ana Silva", service.listarTodos(usuarioA).get(0).getNome());
    }

    @Test
    void excluiSomenteFuncionarioDoProprietario() throws SQLException {
        Funcionario funcionario = service.salvar(novoFuncionario(null, "Ana Souza"), usuarioA);

        assertThrows(IllegalArgumentException.class, () -> service.excluir(funcionario.getUuid(), usuarioB));
        assertEquals(1, service.listarTodos(usuarioA).size());
        service.excluir(funcionario.getUuid(), usuarioA);
        assertTrue(service.listarTodos(usuarioA).isEmpty());
    }

    @Test
    void exigeUsuarioAutenticadoParaTodasOperacoes() {
        Funcionario funcionario = novoFuncionario(null, "Ana Souza");

        assertThrows(IllegalArgumentException.class, () -> service.listarTodos(null));
        assertThrows(IllegalArgumentException.class, () -> service.salvar(funcionario, null));
        assertThrows(IllegalArgumentException.class, () -> service.excluir(UUID.randomUUID(), null));
    }

    @Test
    void rejeitaDataForaDoPeriodoAntesDeAcessarDao() {
        Funcionario funcionario = new Funcionario(null, "Ana Souza", LocalDate.of(1990, 12, 31), new BigDecimal("2500"), true);

        assertThrows(IllegalArgumentException.class, () -> service.salvar(funcionario, usuarioA));
        assertTrue(dao.dados.isEmpty());
    }

    private static Funcionario novoFuncionario(UUID uuid, String nome) {
        return new Funcionario(uuid, nome, LocalDate.of(2020, 6, 15), new BigDecimal("2500.00"), true);
    }

    private static final class InMemoryFuncionarioDAO extends FuncionarioDAO {
        private final Map<UUID, Map<UUID, Funcionario>> dados = new HashMap<>();

        @Override
        public List<Funcionario> listarTodos(UUID usuarioUuid) {
            return dados.getOrDefault(usuarioUuid, Map.of()).values().stream()
                    .map(InMemoryFuncionarioDAO::copiar)
                    .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        }

        @Override
        public Funcionario inserir(Funcionario funcionario, UUID usuarioUuid) {
            if (funcionario.getUuid() == null) {
                funcionario.setUuid(UUID.randomUUID());
            }
            dados.computeIfAbsent(usuarioUuid, ignorado -> new HashMap<>()).put(funcionario.getUuid(), copiar(funcionario));
            return funcionario;
        }

        @Override
        public boolean atualizar(Funcionario funcionario, UUID usuarioUuid) {
            Map<UUID, Funcionario> doUsuario = dados.get(usuarioUuid);
            if (doUsuario == null || !doUsuario.containsKey(funcionario.getUuid())) {
                return false;
            }
            doUsuario.put(funcionario.getUuid(), copiar(funcionario));
            return true;
        }

        @Override
        public boolean excluir(UUID uuid, UUID usuarioUuid) {
            Map<UUID, Funcionario> doUsuario = dados.get(usuarioUuid);
            return doUsuario != null && doUsuario.remove(uuid) != null;
        }

        private static Funcionario copiar(Funcionario funcionario) {
            return new Funcionario(funcionario.getUuid(), funcionario.getNome(), funcionario.getDataAdmissao(), funcionario.getSalario(), funcionario.isStatus());
        }
    }
}
