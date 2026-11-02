package br.com.sistemagestao.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

class FormatadoresTest {

    @Test
    void formataDataEmDiaMesAno() {
        assertEquals("02/11/2026", Formatadores.data(LocalDate.of(2026, 11, 2)));
    }

    @Test
    void retornaTextoVazioParaDataNula() {
        assertEquals("", Formatadores.data(null));
    }

    @Test
    void formataMoedaEmRealBrasileiro() {
        String moeda = Formatadores.moeda(new BigDecimal("1234.56"));
        assertTrue(moeda.startsWith("R$"));
        assertTrue(moeda.contains("1.234,56"));
    }

    @Test
    void retornaTextoVazioParaMoedaNula() {
        assertEquals("", Formatadores.moeda(null));
    }
}
