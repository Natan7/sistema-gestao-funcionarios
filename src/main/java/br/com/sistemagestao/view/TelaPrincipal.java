package br.com.sistemagestao.view;

import br.com.sistemagestao.model.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TelaPrincipal extends JFrame {

    private static final Color PETROLEO = new Color(17, 61, 75);
    private final Usuario usuario;

    public TelaPrincipal(Usuario usuario) {
        super("Gestão de Funcionários | Painel");
        this.usuario = usuario;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(850, 580));
        setSize(1080, 700);
        setLocationRelativeTo(null);
        setContentPane(criarConteudo());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evento) {
                sair();
            }
        });
    }

    private JPanel criarConteudo() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(new Color(246, 248, 249));
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Color.WHITE);
        topo.setBorder(BorderFactory.createEmptyBorder(16, 28, 16, 28));
        JLabel marca = new JLabel("GESTÃO DE FUNCIONÁRIOS");
        marca.setForeground(PETROLEO);
        marca.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel identificacao = new JLabel(usuario.getNome());
        identificacao.setForeground(new Color(65, 79, 84));
        topo.add(marca, BorderLayout.WEST);
        topo.add(identificacao, BorderLayout.EAST);
        raiz.add(topo, BorderLayout.NORTH);

        JPanel navegacao = new JPanel(new BorderLayout());
        navegacao.setBackground(PETROLEO);
        navegacao.setPreferredSize(new Dimension(235, 0));
        navegacao.setBorder(BorderFactory.createEmptyBorder(24, 16, 20, 16));
        JButton funcionarios = new JButton("Funcionários");
        funcionarios.setHorizontalAlignment(SwingConstants.LEFT);
        funcionarios.setBackground(new Color(36, 104, 119));
        funcionarios.setForeground(Color.WHITE);
        funcionarios.setFocusPainted(false);
        funcionarios.setBorder(BorderFactory.createEmptyBorder(13, 16, 13, 12));
        funcionarios.addActionListener(evento -> new TelaFuncionarios(this).setVisible(true));
        JButton sair = new JButton("Sair da conta");
        sair.setHorizontalAlignment(SwingConstants.LEFT);
        sair.setForeground(new Color(221, 233, 235));
        sair.setContentAreaFilled(false);
        sair.setBorderPainted(false);
        sair.setFocusPainted(false);
        sair.addActionListener(evento -> sair());
        navegacao.add(funcionarios, BorderLayout.NORTH);
        navegacao.add(sair, BorderLayout.SOUTH);
        raiz.add(navegacao, BorderLayout.WEST);

        JPanel conteudo = new JPanel(new GridBagLayout());
        conteudo.setOpaque(false);
        JPanel boasVindas = new JPanel(new GridBagLayout());
        boasVindas.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(7, 0, 7, 0);
        JLabel titulo = new JLabel("Olá, " + usuario.getNome().split(" ")[0]);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        titulo.setForeground(new Color(30, 44, 51));
        boasVindas.add(titulo, c);
        c.gridy = 1;
        JLabel descricao = new JLabel("Acesse a área de funcionários para gerenciar sua equipe.");
        descricao.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descricao.setForeground(new Color(96, 111, 117));
        boasVindas.add(descricao, c);
        c.gridy = 2;
        c.insets = new Insets(20, 0, 0, 0);
        JButton abrir = new JButton("Abrir funcionários");
        abrir.setBackground(PETROLEO);
        abrir.setForeground(Color.WHITE);
        abrir.setFocusPainted(false);
        abrir.setPreferredSize(new Dimension(200, 42));
        abrir.addActionListener(evento -> new TelaFuncionarios(this).setVisible(true));
        boasVindas.add(abrir, c);
        conteudo.add(boasVindas);
        raiz.add(conteudo, BorderLayout.CENTER);
        return raiz;
    }

    private void sair() {
        int resposta = javax.swing.JOptionPane.showConfirmDialog(this, "Deseja sair da sua conta?", "Confirmar saída", javax.swing.JOptionPane.YES_NO_OPTION);
        if (resposta == javax.swing.JOptionPane.YES_OPTION) {
            dispose();
            new TelaLogin().setVisible(true);
        }
    }
}