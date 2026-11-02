package br.com.sistemagestao.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import br.com.sistemagestao.dao.UsuarioDAO;
import br.com.sistemagestao.model.Usuario;
import br.com.sistemagestao.util.Criptografia;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsuarioServiceTest {

    private InMemoryUsuarioDAO dao;
    private UsuarioService service;

    @BeforeEach
    void configurar() {
        dao = new InMemoryUsuarioDAO();
        service = new UsuarioService(dao);
    }

    @Test
    void cadastraComEmailNormalizadoEArmazenaSomenteHash() throws SQLException {
        char[] senha = "SenhaSegura123".toCharArray();
        Usuario usuario = service.cadastrar("  Ana Souza  ", "ana@example.com", senha);

        assertEquals("Ana Souza", usuario.getNome());
        assertEquals("ana@example.com", usuario.getEmail());
        assertEquals(Criptografia.sha256(senha), usuario.getSenha());
        assertTrue(usuario.getUuid() != null);
        assertArrayEquals(Criptografia.sha256(senha).toCharArray(), dao.senhaRecebida);
    }

    @Test
    void rejeitaCadastroComDadosInvalidosSemGravar() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar("A", "ana@example.com", "SenhaSegura".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar("Ana Souza", "ana @example.com", "SenhaSegura".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar("Ana Souza", "ana@example.com", "senha com espaco".toCharArray()));
        assertTrue(dao.usuarios.isEmpty());
    }

    @Test
    void rejeitaEmailDuplicado() throws SQLException {
        service.cadastrar("Ana Souza", "ana@example.com", "SenhaSegura".toCharArray());

        SQLIntegrityConstraintViolationException erro = assertThrows(
                SQLIntegrityConstraintViolationException.class,
                () -> service.cadastrar("Outra Ana", "ANA@example.com", "OutraSenha123".toCharArray()));

        assertEquals("23505", erro.getSQLState());
        assertEquals(1, dao.usuarios.size());
    }

    @Test
    void autenticaComSenhaCorretaERejeitaSenhaErradaOuEspacada() throws SQLException {
        Usuario cadastrado = service.cadastrar("Ana Souza", "ana@example.com", "SenhaSegura".toCharArray());

        assertEquals(Optional.of(cadastrado), service.autenticar("ANA@example.com", "SenhaSegura".toCharArray()));
        assertEquals(Optional.empty(), service.autenticar("ana@example.com", "SenhaErrada".toCharArray()));
        assertEquals(Optional.empty(), service.autenticar("ana@example.com", "Senha Segura".toCharArray()));
        assertEquals(Optional.empty(), service.autenticar("email-invalido", "SenhaSegura".toCharArray()));
    }

    @Test
    void entradasInvalidasDeAutenticacaoNaoConsultamDao() throws SQLException {
        assertEquals(Optional.empty(), service.autenticar("ana@example.com", new char[0]));
        assertEquals(Optional.empty(), service.autenticar("ana@example.com", null));
        assertEquals(0, dao.consultasEmail);
    }

    private static final class InMemoryUsuarioDAO extends UsuarioDAO {
        private final Map<String, Usuario> usuarios = new HashMap<>();
        private char[] senhaRecebida;
        private int consultasEmail;

        @Override
        public Optional<Usuario> buscarPorEmail(String email) {
            consultasEmail++;
            return Optional.ofNullable(usuarios.get(email.toLowerCase()));
        }

        @Override
        public boolean emailExiste(String email) {
            return usuarios.containsKey(email.toLowerCase());
        }

        @Override
        public Usuario inserir(Usuario usuario) {
            senhaRecebida = usuario.getSenha().toCharArray();
            if (usuario.getUuid() == null) {
                usuario.setUuid(UUID.randomUUID());
            }
            usuario.setDataCadastro(LocalDateTime.now());
            usuarios.put(usuario.getEmail().toLowerCase(), usuario);
            return usuario;
        }
    }
}
