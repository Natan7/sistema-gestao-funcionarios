package br.com.sistemagestao.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ValidacaoTest {

    @Test
    void aceitaEmailValidoSemEspacos() {
        assertTrue(Validacao.emailValido("equipe@empresa.com"));
    }

    @Test
    void rejeitaEmailComEspacosEmQualquerPosicao() {
        assertFalse(Validacao.emailValido(" equipe@empresa.com"));
        assertFalse(Validacao.emailValido("equipe @empresa.com"));
        assertFalse(Validacao.emailValido("equipe@empresa.com\u00a0"));
    }

    @Test
    void detectaEspacosEmSenha() {
        assertTrue(Validacao.contemEspacos("senha com espaço".toCharArray()));
        assertTrue(!Validacao.contemEspacos("Senha123!".toCharArray()));
    }

    @Test
    void rejeitaEmailSemDominio() {
        assertFalse(Validacao.emailValido("equipe@empresa"));
    }

    @Test
    void aceitaAdmissaoNoPrimeiroDiaDe1991ENaDataAtual() {
        LocalDate hoje = LocalDate.of(2026, 11, 2);
        assertDoesNotThrow(() -> validarFuncionario(LocalDate.of(1991, 1, 1), hoje));
        assertDoesNotThrow(() -> validarFuncionario(hoje, hoje));
    }

    @Test
    void rejeitaAdmissaoAte1990EFuturas() {
        LocalDate hoje = LocalDate.of(2026, 11, 2);
        assertThrows(IllegalArgumentException.class, () -> validarFuncionario(LocalDate.of(1990, 12, 31), hoje));
        assertThrows(IllegalArgumentException.class, () -> validarFuncionario(LocalDate.of(2026, 11, 3), hoje));
    }

    @Test
    void exigeNomeDataESalarioValidos() {
        LocalDate hoje = LocalDate.of(2026, 11, 2);
        assertThrows(IllegalArgumentException.class, () -> Validacao.validarFuncionario("A", LocalDate.of(2020, 1, 1), new BigDecimal("2500.00"), hoje));
        assertThrows(IllegalArgumentException.class, () -> Validacao.validarFuncionario("Ana Souza", null, new BigDecimal("2500.00"), hoje));
        assertThrows(IllegalArgumentException.class, () -> Validacao.validarFuncionario("Ana Souza", LocalDate.of(2020, 1, 1), BigDecimal.ZERO, hoje));
        assertThrows(IllegalArgumentException.class, () -> Validacao.validarFuncionario("Ana Souza", LocalDate.of(2020, 1, 1), new BigDecimal("-1"), hoje));
    }

    private static void validarFuncionario(LocalDate admissao, LocalDate hoje) {
        Validacao.validarFuncionario("Ana Souza", admissao, new BigDecimal("2500.00"), hoje);
    }
}