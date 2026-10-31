package br.com.sistemagestao.view;

import br.com.sistemagestao.controller.LoginController;
import br.com.sistemagestao.model.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class TelaLogin extends JFrame {

    private static final Color PETROLEO = new Color(17, 61, 75);
    private static final Color FUNDO = new Color(246, 248, 249);
    private final LoginController controller = new LoginController();
    private final JTextField campoEmail = new JTextField(25);
    private final JPasswordField campoSenha = new JPasswordField(25);
    private final JLabel mensagem = new JLabel(" ");

    public TelaLogin() {
        super("Gestão de Funcionários | Entrar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 520));
        setSize(900, 590);
        setLocationRelativeTo(null);
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
        estilizarCampo(campoEmail);
        adicionar(formulario, campoEmail, c, 3);
        adicionar(formulario, rotulo("Senha"), c, 4);
        estilizarCampo(campoSenha);
        adicionar(formulario, campoSenha, c, 5);
        mensagem.setForeground(new Color(176, 55, 55));
        mensagem.setFont(new Font("SansSerif", Font.PLAIN, 12));
        adicionar(formulario, mensagem, c, 6);

        JButton entrar = botaoEntrar();
        entrar.addActionListener(evento -> entrar());
        getRootPane().setDefaultButton(entrar);
        c.insets = new Insets(14, 0, 7, 0);
        adicionar(formulario, entrar, c, 7);
        JButton criarConta = new JButton("Criar conta");
        criarConta.setForeground(PETROLEO);
        criarConta.setContentAreaFilled(false);
        criarConta.setBorderPainted(false);
        criarConta.setFocusPainted(false);
        criarConta.addActionListener(evento -> new TelaCadastroUsuario(this).setVisible(true));
        c.insets = new Insets(4, 0, 0, 0);
        adicionar(formulario, criarConta, c, 8);

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

    private void entrar() {
        String email = campoEmail.getText().trim();
        char[] senha = campoSenha.getPassword();
        mensagem.setText(" ");
        try {
            if (email.isEmpty()) {
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