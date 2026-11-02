package br.com.sistemagestao.util;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

public final class FiltroSemEspacos extends DocumentFilter {

    private FiltroSemEspacos() {
    }

    public static void aplicar(JTextComponent campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new FiltroSemEspacos());
    }

    @Override
    public void insertString(FilterBypass bypass, int offset, String texto, AttributeSet atributos) throws BadLocationException {
        if (!Validacao.contemEspacos(texto)) {
            bypass.insertString(offset, texto, atributos);
        }
    }

    @Override
    public void replace(FilterBypass bypass, int offset, int comprimento, String texto, AttributeSet atributos) throws BadLocationException {
        if (!Validacao.contemEspacos(texto)) {
            bypass.replace(offset, comprimento, texto, atributos);
        }
    }
}