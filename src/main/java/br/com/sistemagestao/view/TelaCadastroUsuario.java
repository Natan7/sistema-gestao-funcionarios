package br.com.sistemagestao.view;

import br.com.sistemagestao.controller.UsuarioController;
import br.com.sistemagestao.util.FiltroSemEspacos;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TelaCadastroUsuario extends JDialog {

    private static final Color PETROLEO = new Color(17, 61, 75);
    private final UsuarioController controller = new UsuarioController();
    private final JTextField campoNome = new JTextField(25);
    private final JTextField campoEmail = new JTextField(25);
    private final JPasswordField campoSenha = new JPasswordField(25);
    private final JLabel nivelSenha = new JLabel("Nível da senha: não informada");
    private final JLabel mensagem = new JLabel(" ");

    public TelaCadastroUsuario(JFrame proprietaria) {
        super(proprietaria, "Criar conta", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(criarConteudo());
        setSize(480, 550);
        setResizable(false);
        setLocationRelativeTo(proprietaria);
    }

    private JPanel criarConteudo() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(Color.WHITE);
        raiz.setBorder(BorderFactory.createEmptyBorder(28, 38, 28, 38));
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 5, 0);
        JLabel titulo = new JLabel("Criar conta");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        inserir(formulario, titulo, c, 0);
        JLabel descricao = new JLabel("Cadastre um acesso para o sistema.");
        descricao.setForeground(new Color(100, 114, 120));
        inserir(formulario, descricao, c, 1);
        campo(formulario, c, 2, "Nome completo", campoNome);
        FiltroSemEspacos.aplicar(campoEmail);
        FiltroSemEspacos.aplicar(campoSenha);
        campo(formulario, c, 4, "E-mail", campoEmail);
        inserir(formulario, rotulo("Senha (mínimo de 8 caracteres)"), c, 6);
        estilizarCampo(campoSenha);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(211, 220, 223)),
            BorderFactory.createEmptyBorder(6, 9, 6, 38)));
        inserir(formulario, sobreporBotaoSenha(campoSenha, criarBotaoVisibilidade(campoSenha)), c, 7);
        nivelSenha.setFont(new Font("SansSerif", Font.PLAIN, 12));
        atualizarNivelSenha();
        campoSenha.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent evento) {
                atualizarNivelSenha();
            }

            @Override
            public void removeUpdate(DocumentEvent evento) {
                atualizarNivelSenha();
            }

            @Override
            public void changedUpdate(DocumentEvent evento) {
                atualizarNivelSenha();
            }
        });
        inserir(formulario, nivelSenha, c, 8);
        mensagem.setForeground(new Color(176, 55, 55));
        inserir(formulario, mensagem, c, 10);
        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.setBackground(PETROLEO);
        cadastrar.setForeground(Color.WHITE);
        cadastrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        cadastrar.setFocusPainted(false);
        cadastrar.setPreferredSize(new Dimension(0, 42));
        cadastrar.addActionListener(evento -> cadastrar());
        c.insets = new Insets(13, 0, 0, 0);
        inserir(formulario, cadastrar, c, 11);
        raiz.add(formulario, BorderLayout.CENTER);
        getRootPane().setDefaultButton(cadastrar);
        return raiz;
    }

    private void cadastrar() {
        char[] senha = campoSenha.getPassword();
        try {
            controller.cadastrar(campoNome.getText(), campoEmail.getText(), senha);
            javax.swing.JOptionPane.showMessageDialog(this, "Conta criada com sucesso. Faça login para continuar.", "Cadastro concluído", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IllegalArgumentException exception) {
            mensagem.setText(exception.getMessage());
        } catch (SQLIntegrityConstraintViolationException exception) {
            mensagem.setText("Este e-mail já está cadastrado.");
        } catch (SQLException exception) {
            mensagem.setText("Não foi possível salvar. Verifique a conexão com o banco de dados.");
        } finally {
            Arrays.fill(senha, '\0');
            campoSenha.setText("");
        }
    }

    private void atualizarNivelSenha() {
        char[] senha = campoSenha.getPassword();
        int classes = 0;
        if (senha.length > 0) {
            boolean minuscula = false;
            boolean maiuscula = false;
            boolean numero = false;
            boolean simbolo = false;
            for (char caractere : senha) {
                minuscula |= Character.isLowerCase(caractere);
                maiuscula |= Character.isUpperCase(caractere);
                numero |= Character.isDigit(caractere);
                simbolo |= !Character.isLetterOrDigit(caractere);
            }
            classes = (minuscula ? 1 : 0) + (maiuscula ? 1 : 0) + (numero ? 1 : 0) + (simbolo ? 1 : 0);
        }

        String nivel;
        Color cor;
        if (senha.length == 0) {
            nivel = "não informada";
            cor = new Color(100, 114, 120);
        } else if (senha.length < 6) {
            nivel = "muito fraca";
            cor = new Color(176, 55, 55);
        } else if (senha.length < 8) {
            nivel = "fraca";
            cor = new Color(190, 105, 35);
        } else if (senha.length >= 14 && classes >= 3) {
            nivel = "muito forte";
            cor = new Color(35, 122, 82);
        } else if (senha.length >= 10 && classes >= 3) {
            nivel = "forte";
            cor = new Color(35, 122, 82);
        } else {
            nivel = "média";
            cor = new Color(166, 132, 25);
        }
        nivelSenha.setText("Nível da senha: " + nivel);
        nivelSenha.setForeground(cor);
        Arrays.fill(senha, '\0');
    }

    private static JButton criarBotaoVisibilidade(JPasswordField campo) {
        JButton botao = new JButton(new FlatSVGIcon("imagens/eye-off.svg", 18, 18));
        char caractereOculto = campo.getEchoChar();
        botao.setFocusable(false);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setMargin(new Insets(2, 4, 2, 4));
        botao.setPreferredSize(new Dimension(28, 28));
        botao.setToolTipText("Mostrar senha");
        botao.addActionListener(evento -> {
            boolean visivel = campo.getEchoChar() != 0;
            campo.setEchoChar(visivel ? (char) 0 : caractereOculto);
            botao.setIcon(new FlatSVGIcon(visivel
                    ? "imagens/eye.svg"
                    : "imagens/eye-off.svg", 18, 18));
            botao.setToolTipText(visivel ? "Ocultar senha" : "Mostrar senha");
        });
        return botao;
    }

    private static JPanel sobreporBotaoSenha(JPasswordField campo, JButton botao) {
        JPanel painel = new JPanel(null) {
            @Override
            public void doLayout() {
                campo.setBounds(0, 0, getWidth(), getHeight());
                Dimension tamanhoBotao = botao.getPreferredSize();
                botao.setBounds(getWidth() - tamanhoBotao.width - 6,
                        (getHeight() - tamanhoBotao.height) / 2,
                        tamanhoBotao.width, tamanhoBotao.height);
            }

            @Override
            public Dimension getPreferredSize() {
                return campo.getPreferredSize();
            }
        };
        painel.setOpaque(false);
        painel.add(campo);
        painel.add(botao);
        painel.setComponentZOrder(botao, 0);
        return painel;
    }

    private static JLabel rotulo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        rotulo.setForeground(new Color(55, 70, 76));
        return rotulo;
    }

    private static void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 40));
        campo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(211, 220, 223)), BorderFactory.createEmptyBorder(6, 9, 6, 9)));
    }

    private static void campo(JPanel painel, GridBagConstraints c, int linha, String texto, JTextField campo) {
        inserir(painel, rotulo(texto), c, linha);
        estilizarCampo(campo);
        inserir(painel, campo, c, linha + 1);
    }

    private static void inserir(JPanel painel, java.awt.Component componente, GridBagConstraints c, int linha) {
        c.gridy = linha;
        painel.add(componente, c);
    }
}