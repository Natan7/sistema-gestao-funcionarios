package br.com.sistemagestao.view;

import br.com.sistemagestao.controller.FuncionarioController;
import br.com.sistemagestao.model.Funcionario;
import br.com.sistemagestao.util.Formatadores;
import br.com.sistemagestao.util.FiltroSalario;
import br.com.sistemagestao.util.Validacao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Comparator;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

public class TelaFuncionarios extends JFrame {

    private static final Color PETROLEO = new Color(17, 61, 75);
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private final FuncionarioController controller = new FuncionarioController();
    private final JTextField campoNome = new JTextField(22);
    private final JFormattedTextField campoAdmissao = criarCampoAdmissao();
    private final JTextField campoSalario = new JTextField(12);
    private final JCheckBox campoStatus = new JCheckBox("Ativo", true);
    private final JLabel mensagem = new JLabel(" ");
    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"UUID", "Nº", "Nome", "Admissão", "Salário", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modelo);
    private final TableRowSorter<DefaultTableModel> ordenador = new TableRowSorter<>(modelo);
    private UUID funcionarioSelecionado;

    public TelaFuncionarios(JFrame proprietaria) {
        super("Gestão de Funcionários | Funcionários");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setSize(1120, 720);
        setLocationRelativeTo(proprietaria);
        setContentPane(criarConteudo());
        carregarFuncionarios();
    }

    private JPanel criarConteudo() {
        JPanel raiz = new JPanel(new BorderLayout(0, 18));
        raiz.setBackground(new Color(246, 248, 249));
        raiz.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        JLabel titulo = new JLabel("Funcionários");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(new Color(30, 44, 51));
        JButton atualizar = new JButton("Atualizar lista");
        atualizar.addActionListener(evento -> carregarFuncionarios());
        cabecalho.add(titulo, BorderLayout.WEST);
        cabecalho.add(atualizar, BorderLayout.EAST);
        raiz.add(cabecalho, BorderLayout.NORTH);

        tabela.setRowHeight(34);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setFillsViewportHeight(true);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        ordenador.setSortable(0, false);
        ordenador.setSortable(1, false);
        ordenador.setComparator(3, Comparator.comparing((String valor) -> LocalDate.parse(valor, FORMATO_DATA)));
        ordenador.setComparator(4, Comparator.comparing((String valor) -> valorNumericoSalario(valor)));
        tabela.setRowSorter(ordenador);
        tabela.removeColumn(tabela.getColumnModel().getColumn(0));
        tabela.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable tabela, Object valor, boolean selecionada,
                    boolean foco, int linha, int coluna) {
                super.getTableCellRendererComponent(tabela, valor, selecionada, foco, linha, coluna);
                setHorizontalAlignment(SwingConstants.CENTER);
                setText(Integer.toString(linha + 1));
                return this;
            }
        });
        tabela.getColumnModel().getColumn(0).setMinWidth(48);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(48);
        tabela.getColumnModel().getColumn(0).setMaxWidth(56);
        tabela.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                preencherFormulario();
            }
        });
        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setBorder(BorderFactory.createLineBorder(new Color(220, 227, 229)));

        JPanel inferior = new JPanel(new BorderLayout(0, 8));
        inferior.setOpaque(false);
        inferior.add(criarFormulario(), BorderLayout.CENTER);
        mensagem.setForeground(new Color(176, 55, 55));
        inferior.add(mensagem, BorderLayout.SOUTH);
        raiz.add(rolagem, BorderLayout.CENTER);
        raiz.add(inferior, BorderLayout.SOUTH);
        return raiz;
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 227, 229)),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        adicionarCampo(painel, c, 0, 0, "Nome", campoNome);
        adicionarCampo(painel, c, 1, 0, "Admissão (DD/MM/AAAA)", campoAdmissao);
        FiltroSalario.aplicar(campoSalario);
        adicionarCampo(painel, c, 2, 0, "Salário", campoSalario);
        c.gridx = 3;
        c.gridy = 1;
        painel.add(campoStatus, c);

        JPanel botoes = new JPanel();
        botoes.setOpaque(false);
        JButton salvar = new JButton("Salvar funcionário");
        salvar.setBackground(PETROLEO);
        salvar.setForeground(Color.WHITE);
        salvar.setFocusPainted(false);
        salvar.addActionListener(evento -> salvar());
        JButton limpar = new JButton("Limpar");
        limpar.addActionListener(evento -> limparFormulario());
        JButton excluir = new JButton("Excluir selecionado");
        excluir.addActionListener(evento -> excluir());
        botoes.add(salvar);
        botoes.add(limpar);
        botoes.add(excluir);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.WEST;
        painel.add(botoes, c);
        return painel;
    }

    private static void adicionarCampo(JPanel painel, GridBagConstraints c, int coluna, int linha, String rotulo, JComponent campo) {
        c.gridx = coluna;
        c.gridy = linha;
        JLabel label = new JLabel(rotulo);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        painel.add(label, c);
        c.gridy = linha + 1;
        campo.setPreferredSize(new Dimension(170, 34));
        painel.add(campo, c);
    }

    private void carregarFuncionarios() {
        try {
            List<Funcionario> funcionarios = controller.listarTodos();
            tabela.clearSelection();
            funcionarioSelecionado = null;
            tabela.setRowSorter(null);
            try {
                modelo.setRowCount(0);
                for (Funcionario funcionario : funcionarios) {
                    modelo.addRow(new Object[]{funcionario.getUuid(), null, funcionario.getNome(), Formatadores.data(funcionario.getDataAdmissao()), Formatadores.moeda(funcionario.getSalario()), funcionario.isStatus() ? "Ativo" : "Inativo"});
                }
            } finally {
                tabela.setRowSorter(ordenador);
            }
            mensagem.setText(" ");
        } catch (SQLException exception) {
            mensagem.setText("Não foi possível carregar os funcionários. Verifique a conexão com o banco.");
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        linha = tabela.convertRowIndexToModel(linha);
        funcionarioSelecionado = (UUID) modelo.getValueAt(linha, 0);
        campoNome.setText((String) modelo.getValueAt(linha, 2));
        campoAdmissao.setText(modelo.getValueAt(linha, 3).toString());
        String salario = modelo.getValueAt(linha, 4).toString().replaceAll("[^0-9,.-]", "");
        if (salario.contains(",")) {
            salario = salario.replace(".", "");
        }
        campoSalario.setText(salario);
        campoStatus.setSelected("Ativo".equals(modelo.getValueAt(linha, 5)));
        mensagem.setText(" ");
    }

    private void salvar() {
        try {
            String textoSalario = campoSalario.getText().replace("R$", "");
            if (Validacao.contemEspacos(textoSalario) || Validacao.contemEspacos(campoAdmissao.getText())) {
                throw new IllegalArgumentException("Data e salário não podem conter espaços.");
            }
            if (textoSalario.contains(",")) {
                textoSalario = textoSalario.replace(".", "").replace(',', '.');
            }
            LocalDate admissao = LocalDate.parse(campoAdmissao.getText(), FORMATO_DATA);
            Funcionario funcionario = new Funcionario(funcionarioSelecionado, campoNome.getText(), admissao, new BigDecimal(textoSalario), campoStatus.isSelected());
            controller.salvar(funcionario);
            limparFormulario();
            carregarFuncionarios();
            mensagem.setForeground(new Color(35, 122, 82));
            mensagem.setText("Funcionário salvo com sucesso.");
        } catch (IllegalArgumentException exception) {
            mensagem.setForeground(new Color(176, 55, 55));
            mensagem.setText(exception.getMessage() == null ? "Confira a data e o salário informados." : exception.getMessage());
        } catch (SQLException exception) {
            mensagem.setForeground(new Color(176, 55, 55));
            mensagem.setText("Não foi possível salvar. Verifique a conexão com o banco de dados.");
        }
    }

    private void excluir() {
        if (funcionarioSelecionado == null) {
            mensagem.setText("Selecione um funcionário na tabela.");
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja excluir este funcionário?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(funcionarioSelecionado);
                limparFormulario();
                carregarFuncionarios();
                mensagem.setForeground(new Color(35, 122, 82));
                mensagem.setText("Funcionário excluído.");
            } catch (SQLException exception) {
                mensagem.setForeground(new Color(176, 55, 55));
                mensagem.setText("Não foi possível excluir o funcionário.");
            }
        }
    }

    private void limparFormulario() {
        funcionarioSelecionado = null;
        tabela.clearSelection();
        campoNome.setText("");
        campoAdmissao.setText("");
        campoSalario.setText("");
        campoStatus.setSelected(true);
    }

    private static JFormattedTextField criarCampoAdmissao() {
        try {
            MaskFormatter formato = new MaskFormatter("##/##/####");
            formato.setPlaceholderCharacter('_');
            formato.setAllowsInvalid(false);
            return new JFormattedTextField(formato);
        } catch (ParseException exception) {
            throw new IllegalStateException("Não foi possível configurar o campo de data.", exception);
        }
    }

    private static BigDecimal valorNumericoSalario(String salarioFormatado) {
        String valor = salarioFormatado.replaceAll("[^0-9,.-]", "")
                .replace(".", "")
                .replace(',', '.');
        return new BigDecimal(valor);
    }
}