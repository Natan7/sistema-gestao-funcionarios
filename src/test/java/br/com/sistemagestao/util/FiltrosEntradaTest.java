package br.com.sistemagestao.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import javax.swing.JTextField;

class FiltrosEntradaTest {

    @Test
    void filtroSemEspacosRejeitaDigitacaoEColagemComEspacos() {
        JTextField campo = new JTextField();
        FiltroSemEspacos.aplicar(campo);

        campo.setText("usuario@example.com");
        assertEquals("usuario@example.com", campo.getText());
        campo.setText("usuario com espaco@example.com");
        assertEquals("usuario@example.com", campo.getText());
    }

    @Test
    void filtroSalarioPermiteDigitosEUmSeparadorDecimal() {
        JTextField campo = new JTextField();
        FiltroSalario.aplicar(campo);

        campo.setText("1234,56");
        assertEquals("1234,56", campo.getText());
        campo.setText("1234.56");
        assertEquals("1234.56", campo.getText());
    }

    @Test
    void filtroSalarioRejeitaLetrasSinaisEspacosESegundoSeparador() {
        JTextField campo = new JTextField();
        FiltroSalario.aplicar(campo);

        campo.setText("100.00");
        campo.setText("12a.00");
        assertEquals("100.00", campo.getText());
        campo.setText("-100");
        assertEquals("100.00", campo.getText());
        campo.setText("1 000");
        assertEquals("100.00", campo.getText());
        campo.setText("1,000.00");
        assertEquals("100.00", campo.getText());
    }
}
