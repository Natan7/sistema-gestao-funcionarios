package br.com.sistemagestao;

import br.com.sistemagestao.view.TelaLogin;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class App {

    private App() {
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 10);
        UIManager.put("TextComponent.arc", 8);
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}