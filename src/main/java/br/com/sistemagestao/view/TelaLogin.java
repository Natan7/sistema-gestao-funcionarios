package br.com.sistemagestao.view;

import br.com.sistemagestao.controller.LoginController;
import br.com.sistemagestao.model.Usuario;
import br.com.sistemagestao.util.FiltroSemEspacos;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TelaLogin extends JFrame {

    private static final Color PETROLEO = new Color(17, 61, 75);
    private static final Color FUNDO = new Color(246, 248, 249);
    private static final String CHAVE_EMAIL_LEMBRADO = "emailLembrado";
    private static final String CHAVE_CONTAGEM_EMAILS = "contagemEmailsUsados";
    private static final String PREFIXO_EMAIL_USADO = "emailUsado.";
    private static final int LIMITE_EMAILS_USADOS = 10;
    private static final Preferences PREFERENCIAS = Preferences.userNodeForPackage(TelaLogin.class);
    private final LoginController controller = new LoginController();
    private final JTextField campoEmail = new JTextField(25);
    private final JPasswordField campoSenha = new JPasswordField(25);
    private final JCheckBox lembrarEmail = new JCheckBox("Lembrar meu e-mail");
    private final DefaultListModel<String> modeloSugestoes = new DefaultListModel<>();
    private final JList<String> listaSugestoes = new JList<>(modeloSugestoes);
    private final JPopupMenu popupSugestoes = new JPopupMenu();
    private final List<String> emailsUsados = new ArrayList<>();
    private final JLabel mensagem = new JLabel(" ");
    private boolean selecionandoSugestao;

    public TelaLogin() {
        super("Gestão de Funcionários | Entrar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 520));
        setSize(900, 590);
        setLocationRelativeTo(null);
        String emailLembrado = PREFERENCIAS.get(CHAVE_EMAIL_LEMBRADO, "");
        if (!emailLembrado.isEmpty()) {
            campoEmail.setText(emailLembrado);
            lembrarEmail.setSelected(true);
            carregarEmailsUsados(emailLembrado);
        }
        lembrarEmail.addActionListener(evento -> {
            if (!lembrarEmail.isSelected()) {
                removerEmailLembrado();
            }
        });
        setContentPane(criarConteudo());
    }

    private JPanel criarConteudo() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FUNDO);
        raiz.add(criarMarca(), BorderLayout.WEST);
        raiz.add(criarFormulario(), BorderLayout.CENTER);
        return raiz;
    }

    private JPanel criarMarca() {
        JPanel marca = new JPanel();
        marca.setBackground(PETROLEO);
        marca.setPreferredSize(new Dimension(330, 0));
        marca.setBorder(BorderFactory.createEmptyBorder(65, 38, 48, 34));
        marca.setLayout(new javax.swing.BoxLayout(marca, javax.swing.BoxLayout.Y_AXIS));

        JLabel sigla = new JLabel("SG", SwingConstants.CENTER);
        sigla.setOpaque(true);
        sigla.setBackground(new Color(36, 104, 119));
        sigla.setForeground(Color.WHITE);
        sigla.setFont(new Font("SansSerif", Font.BOLD, 24));
        sigla.setPreferredSize(new Dimension(60, 60));
        sigla.setMaximumSize(new Dimension(60, 60));
        sigla.setAlignmentX(LEFT_ALIGNMENT);
        JLabel titulo = new JLabel("Gestão de\nFuncionários");
        titulo.setText("<html>Gestão de<br>Funcionários</html>");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        JLabel descricao = new JLabel("<html>Um espaço claro para<br>cuidar da sua equipe.</html>");
        descricao.setForeground(new Color(207, 224, 228));
        descricao.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descricao.setAlignmentX(LEFT_ALIGNMENT);
        marca.add(sigla);
        marca.add(javax.swing.Box.createVerticalStrut(38));
        marca.add(titulo);
        marca.add(javax.swing.Box.createVerticalStrut(18));
        marca.add(descricao);
        return marca;
    }

    private JPanel criarFormulario() {
        JPanel area = new JPanel(new GridBagLayout());
        area.setBackground(FUNDO);
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(FUNDO);
        formulario.setBorder(BorderFactory.createEmptyBorder(30, 42, 30, 42));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 5, 0);

        JLabel titulo = new JLabel("Acesse sua conta");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        titulo.setForeground(new Color(30, 44, 51));
        adicionar(formulario, titulo, c, 0);
        JLabel subtitulo = new JLabel("Entre para continuar para o painel.");
        subtitulo.setForeground(new Color(102, 116, 122));
        adicionar(formulario, subtitulo, c, 1);
        adicionar(formulario, rotulo("E-mail"), c, 2);
        FiltroSemEspacos.aplicar(campoEmail);
        FiltroSemEspacos.aplicar(campoSenha);
        estilizarCampo(campoEmail);
        adicionar(formulario, campoEmail, c, 3);
        configurarAutocompleteEmail();
        adicionar(formulario, rotulo("Senha"), c, 4);
        estilizarCampo(campoSenha);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(211, 220, 223)),
            BorderFactory.createEmptyBorder(7, 10, 7, 38)));
        adicionar(formulario, sobreporBotaoSenha(criarBotaoVisibilidade()), c, 5);
        lembrarEmail.setOpaque(false);
        lembrarEmail.setForeground(new Color(55, 70, 76));
        lembrarEmail.setFont(new Font("SansSerif", Font.PLAIN, 12));
        adicionar(formulario, lembrarEmail, c, 6);
        mensagem.setForeground(new Color(176, 55, 55));
        mensagem.setFont(new Font("SansSerif", Font.PLAIN, 12));
        adicionar(formulario, mensagem, c, 7);

        JButton entrar = botaoEntrar();
        entrar.addActionListener(evento -> entrar());
        getRootPane().setDefaultButton(entrar);
        c.insets = new Insets(14, 0, 7, 0);
        adicionar(formulario, entrar, c, 8);
        JButton criarConta = new JButton("Criar conta");
        criarConta.setForeground(PETROLEO);
        criarConta.setContentAreaFilled(false);
        criarConta.setBorderPainted(false);
        criarConta.setFocusPainted(false);
        criarConta.addActionListener(evento -> new TelaCadastroUsuario(this).setVisible(true));
        c.insets = new Insets(4, 0, 0, 0);
        adicionar(formulario, criarConta, c, 9);

        area.add(formulario);
        return area;
    }

    private JButton botaoEntrar() {
        JButton entrar = new JButton("Entrar");
        entrar.setBackground(PETROLEO);
        entrar.setForeground(Color.WHITE);
        entrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        entrar.setFocusPainted(false);
        entrar.setPreferredSize(new Dimension(0, 44));
        return entrar;
    }

    private JButton criarBotaoVisibilidade() {
        JButton botao = new JButton(new FlatSVGIcon("imagens/eye-off.svg", 18, 18));
        char caractereOculto = campoSenha.getEchoChar();
        botao.setFocusable(false);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setMargin(new Insets(2, 4, 2, 4));
        botao.setPreferredSize(new Dimension(28, 28));
        botao.setToolTipText("Mostrar senha");
        botao.addActionListener(evento -> {
            boolean visivel = campoSenha.getEchoChar() != 0;
            campoSenha.setEchoChar(visivel ? (char) 0 : caractereOculto);
            botao.setIcon(new FlatSVGIcon(visivel
                    ? "imagens/eye.svg"
                    : "imagens/eye-off.svg", 18, 18));
            botao.setToolTipText(visivel ? "Ocultar senha" : "Mostrar senha");
        });
        return botao;
    }

    private JPanel sobreporBotaoSenha(JButton botao) {
        JPanel painel = new JPanel(null) {
            @Override
            public void doLayout() {
                campoSenha.setBounds(0, 0, getWidth(), getHeight());
                Dimension tamanhoBotao = botao.getPreferredSize();
                botao.setBounds(getWidth() - tamanhoBotao.width - 6,
                        (getHeight() - tamanhoBotao.height) / 2,
                        tamanhoBotao.width, tamanhoBotao.height);
            }

            @Override
            public Dimension getPreferredSize() {
                return campoSenha.getPreferredSize();
            }
        };
        painel.setOpaque(false);
        painel.add(campoSenha);
        painel.add(botao);
        painel.setComponentZOrder(botao, 0);
        return painel;
    }

    private void entrar() {
        String email = campoEmail.getText();
        char[] senha = campoSenha.getPassword();
        mensagem.setText(" ");
        try {
            if (email.isBlank()) {
                mensagem.setText("Informe seu e-mail.");
                return;
            }
            if (senha.length == 0) {
                mensagem.setText("Informe sua senha.");
                return;
            }
            if (!br.com.sistemagestao.util.Validacao.emailValido(email)) {
                mensagem.setText("E-mail inválido.");
                return;
            }
            Optional<Usuario> usuario = controller.autenticar(email, senha);
            if (usuario.isPresent()) {
                salvarEmailLembrado(email);
                if (lembrarEmail.isSelected()) {
                    registrarEmailUsado(email);
                }
                dispose();
                new TelaPrincipal(usuario.get()).setVisible(true);
            } else {
                mensagem.setText("E-mail ou senha incorretos.");
            }
        } catch (SQLException exception) {
            mensagem.setText("Não foi possível conectar ao banco de dados.");
        } finally {
            Arrays.fill(senha, '\0');
            campoSenha.setText("");
        }
    }

    private void salvarEmailLembrado(String email) {
        atualizarPreferenciaEmail(lembrarEmail.isSelected() ? email : null);
    }

    private void removerEmailLembrado() {
        try {
            PREFERENCIAS.remove(CHAVE_EMAIL_LEMBRADO);
            for (int indice = 0; indice < LIMITE_EMAILS_USADOS; indice++) {
                PREFERENCIAS.remove(PREFIXO_EMAIL_USADO + indice);
            }
            PREFERENCIAS.putInt(CHAVE_CONTAGEM_EMAILS, 0);
            PREFERENCIAS.flush();
            emailsUsados.clear();
            modeloSugestoes.clear();
            popupSugestoes.setVisible(false);
        } catch (BackingStoreException | SecurityException exception) {
            mensagem.setText("Não foi possível atualizar a preferência de e-mail neste computador.");
        }
    }

    private void atualizarPreferenciaEmail(String email) {
        try {
            if (email == null) {
                PREFERENCIAS.remove(CHAVE_EMAIL_LEMBRADO);
            } else {
                PREFERENCIAS.put(CHAVE_EMAIL_LEMBRADO, email);
            }
            PREFERENCIAS.flush();
        } catch (BackingStoreException | SecurityException exception) {
            mensagem.setText("Não foi possível atualizar a preferência de e-mail neste computador.");
        }
    }

    private void carregarEmailsUsados(String emailLembrado) {
        int quantidade = Math.min(PREFERENCIAS.getInt(CHAVE_CONTAGEM_EMAILS, 0), LIMITE_EMAILS_USADOS);
        for (int indice = 0; indice < quantidade; indice++) {
            String email = PREFERENCIAS.get(PREFIXO_EMAIL_USADO + indice, "");
            if (!email.isBlank()) {
                emailsUsados.add(email);
            }
        }
        adicionarEmailEmMemoria(emailLembrado);
    }

    private void registrarEmailUsado(String email) {
        adicionarEmailEmMemoria(email);
        try {
            for (int indice = 0; indice < LIMITE_EMAILS_USADOS; indice++) {
                PREFERENCIAS.remove(PREFIXO_EMAIL_USADO + indice);
            }
            int quantidade = Math.min(emailsUsados.size(), LIMITE_EMAILS_USADOS);
            for (int indice = 0; indice < quantidade; indice++) {
                PREFERENCIAS.put(PREFIXO_EMAIL_USADO + indice, emailsUsados.get(indice));
            }
            PREFERENCIAS.putInt(CHAVE_CONTAGEM_EMAILS, quantidade);
            PREFERENCIAS.flush();
        } catch (BackingStoreException | SecurityException exception) {
            mensagem.setText("Não foi possível atualizar as sugestões de e-mail neste computador.");
        }
    }

    private void adicionarEmailEmMemoria(String email) {
        emailsUsados.removeIf(emailExistente -> emailExistente.equalsIgnoreCase(email));
        emailsUsados.add(0, email);
        if (emailsUsados.size() > LIMITE_EMAILS_USADOS) {
            emailsUsados.remove(emailsUsados.size() - 1);
        }
    }

    private void configurarAutocompleteEmail() {
        listaSugestoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaSugestoes.setFont(campoEmail.getFont());
        listaSugestoes.setFixedCellHeight(28);
        listaSugestoes.setFocusable(false);
        JScrollPane rolagemSugestoes = new JScrollPane(listaSugestoes);
        rolagemSugestoes.setBorder(BorderFactory.createEmptyBorder());
        popupSugestoes.setBorder(BorderFactory.createLineBorder(new Color(211, 220, 223)));
        popupSugestoes.add(rolagemSugestoes);

        campoEmail.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent evento) {
                atualizarSugestoesEmail();
            }

            @Override
            public void removeUpdate(DocumentEvent evento) {
                atualizarSugestoesEmail();
            }

            @Override
            public void changedUpdate(DocumentEvent evento) {
                atualizarSugestoesEmail();
            }
        });
        listaSugestoes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                int indice = listaSugestoes.locationToIndex(evento.getPoint());
                if (indice >= 0 && listaSugestoes.getCellBounds(indice, indice).contains(evento.getPoint())) {
                    selecionarSugestao(indice);
                }
            }
        });
        campoEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evento) {
                if (!popupSugestoes.isVisible()) {
                    return;
                }
                switch (evento.getKeyCode()) {
                    case KeyEvent.VK_DOWN -> {
                        moverSelecaoSugestao(1);
                        evento.consume();
                    }
                    case KeyEvent.VK_UP -> {
                        moverSelecaoSugestao(-1);
                        evento.consume();
                    }
                    case KeyEvent.VK_ENTER -> {
                        if (listaSugestoes.getSelectedIndex() >= 0) {
                            selecionarSugestao(listaSugestoes.getSelectedIndex());
                            evento.consume();
                        }
                    }
                    case KeyEvent.VK_ESCAPE -> {
                        popupSugestoes.setVisible(false);
                        evento.consume();
                    }
                    default -> {
                    }
                }
            }
        });
    }

    private void atualizarSugestoesEmail() {
        if (selecionandoSugestao || !lembrarEmail.isSelected()) {
            popupSugestoes.setVisible(false);
            return;
        }
        String digitado = campoEmail.getText();
        modeloSugestoes.clear();
        if (digitado.isEmpty()) {
            popupSugestoes.setVisible(false);
            return;
        }
        for (String email : emailsUsados) {
            if (email.regionMatches(true, 0, digitado, 0, digitado.length())
                    && !email.equalsIgnoreCase(digitado)) {
                modeloSugestoes.addElement(email);
            }
        }
        if (modeloSugestoes.isEmpty() || !campoEmail.isShowing()) {
            popupSugestoes.setVisible(false);
            return;
        }
        listaSugestoes.setSelectedIndex(0);
        int altura = Math.min(modeloSugestoes.size(), 5) * listaSugestoes.getFixedCellHeight() + 2;
        popupSugestoes.setPreferredSize(new Dimension(campoEmail.getWidth(), altura));
        popupSugestoes.show(campoEmail, 0, campoEmail.getHeight());
    }

    private void moverSelecaoSugestao(int deslocamento) {
        int tamanho = modeloSugestoes.size();
        if (tamanho == 0) {
            return;
        }
        int indice = listaSugestoes.getSelectedIndex();
        indice = indice < 0 ? 0 : Math.floorMod(indice + deslocamento, tamanho);
        listaSugestoes.setSelectedIndex(indice);
        listaSugestoes.ensureIndexIsVisible(indice);
    }

    private void selecionarSugestao(int indice) {
        if (indice < 0 || indice >= modeloSugestoes.size()) {
            return;
        }
        selecionandoSugestao = true;
        campoEmail.setText(modeloSugestoes.getElementAt(indice));
        campoEmail.setCaretPosition(campoEmail.getText().length());
        popupSugestoes.setVisible(false);
        selecionandoSugestao = false;
    }

    private static JLabel rotulo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setForeground(new Color(55, 70, 76));
        rotulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        return rotulo;
    }

    private static void estilizarCampo(javax.swing.JComponent campo) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 42));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(211, 220, 223)),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));
    }

    private static void adicionar(JPanel painel, java.awt.Component componente, GridBagConstraints c, int linha) {
        c.gridy = linha;
        painel.add(componente, c);
    }
}