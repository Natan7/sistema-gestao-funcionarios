package br.com.sistemagestao.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ValidacaoTest {

    @Test
    void aceitaEmailValidoComEspacosExternos() {
        assertTrue(Validacao.emailValido("  equipe@empresa.com  "));
    }

    @Test
    void rejeitaEmailSemDominio() {
        assertTrue(!Validacao.emailValido("equipe@empresa"));
    }

    @Test
    void exigeSalarioPositivo() {
        assertDoesNotThrow(() -> Validacao.validarFuncionario("Ana Souza", LocalDate.now(), new BigDecimal("2500.00")));
        assertThrows(IllegalArgumentException.class, () -> Validacao.validarFuncionario("Ana Souza", LocalDate.now(), BigDecimal.ZERO));
    }
}