package br.com.sistemagestao.view;

import br.com.sistemagestao.controller.UsuarioController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

public class TelaCadastroUsuario extends JDialog {

    private static final Color PETROLEO = new Color(17, 61, 75);
    private final UsuarioController controller = new UsuarioController();
    private final JTextField campoNome = new JTextField(25);
    private final JTextField campoEmail = new JTextField(25);
    private final JPasswordField campoSenha = new JPasswordField(25);
    private final JLabel mensagem = new JLabel(" ");

    public TelaCadastroUsuario(JFrame proprietaria) {
        super(proprietaria, "Criar conta", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(criarConteudo());
        setSize(480, 520);
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
        campo(formulario, c, 4, "E-mail", campoEmail);
        campo(formulario, c, 6, "Senha (mínimo de 8 caracteres)", campoSenha);
        mensagem.setForeground(new Color(176, 55, 55));
        inserir(formulario, mensagem, c, 8);
        JButton cadastrar = new JButton("Cadastrar");
        cadastrar.setBackground(PETROLEO);
        cadastrar.setForeground(Color.WHITE);
        cadastrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        cadastrar.setFocusPainted(false);
        cadastrar.setPreferredSize(new Dimension(0, 42));
        cadastrar.addActionListener(evento -> cadastrar());
        c.insets = new Insets(13, 0, 0, 0);
        inserir(formulario, cadastrar, c, 9);
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

    private static void campo(JPanel painel, GridBagConstraints c, int linha, String texto, JTextField campo) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        rotulo.setForeground(new Color(55, 70, 76));
        inserir(painel, rotulo, c, linha);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 40));
        campo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(211, 220, 223)), BorderFactory.createEmptyBorder(6, 9, 6, 9)));
        inserir(painel, campo, c, linha + 1);
    }

    private static void inserir(JPanel painel, java.awt.Component componente, GridBagConstraints c, int linha) {
        c.gridy = linha;
        painel.add(componente, c);
    }
}