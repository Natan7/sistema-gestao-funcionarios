package br.com.sistemagestao.util;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

public final class FiltroSalario extends DocumentFilter {

    private FiltroSalario() {
    }

    public static void aplicar(JTextComponent campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new FiltroSalario());
    }

    @Override
    public void insertString(FilterBypass bypass, int offset, String texto, AttributeSet atributos) throws BadLocationException {
        substituir(bypass, offset, 0, texto, atributos);
    }

    @Override
    public void replace(FilterBypass bypass, int offset, int comprimento, String texto, AttributeSet atributos) throws BadLocationException {
        substituir(bypass, offset, comprimento, texto, atributos);
    }

    private void substituir(FilterBypass bypass, int offset, int comprimento, String texto, AttributeSet atributos) throws BadLocationException {
        String atual = bypass.getDocument().getText(0, bypass.getDocument().getLength());
        String inserido = texto == null ? "" : texto;
        String proposto = atual.substring(0, offset) + inserido + atual.substring(offset + comprimento);
        if (proposto.matches("[0-9]*([.,][0-9]*)?")) {
            bypass.replace(offset, comprimento, inserido, atributos);
        }
    }
}
