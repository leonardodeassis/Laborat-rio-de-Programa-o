package biblioteca.tela;

import biblioteca.dao.EmprestimoDAO;
import biblioteca.dao.LivroDAO;
import biblioteca.dao.MembroDAO;
import biblioteca.modelo.Emprestimo;
import biblioteca.modelo.Livro;
import biblioteca.modelo.Membro;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Tela de gerenciamento de Emprestimos.
 *
 * Os JComboBox guardam objetos Livro / Membro inteiros. O toString() deles
 * mostra "id - titulo" / "id - nome", mas o id real fica guardado no objeto
 * (getSelectedItem() devolve o objeto, e pegamos o id dele).
 *
 * Ligacao botao -> banco (via EmprestimoDAO):
 *   Realizar Emprestimo -> realizar() (INSERT)
 *   Encerrar Emprestimo -> encerrar() (UPDATE data_fim)
 *   Listar              -> listar()   (SELECT com JOIN) filtrando por Todos/Ativos/Finalizados
 *
 * Formato de data: aaaa-mm-dd (padrao aceito pelo java.sql.Date.valueOf).
 */
public class TelaEmprestimos extends JFrame {

    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private final LivroDAO livroDAO = new LivroDAO();
    private final MembroDAO membroDAO = new MembroDAO();

    private JComboBox<Livro> cbLivro;
    private JComboBox<Membro> cbMembro;
    private JTextField txtDataInicio;
    private JTextField txtDataFim;
    private JComboBox<String> cbFiltro;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaEmprestimos() {
        super("Gerenciamento de Emprestimos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        montarTela();
        setSize(800, 520);
        setLocationRelativeTo(null);
        carregarCombos();
        listar();
    }

    private void montarTela() {
        cbLivro = new JComboBox<>();
        cbMembro = new JComboBox<>();
        // data de inicio ja vem com a data de hoje
        txtDataInicio = new JTextField(new Date(System.currentTimeMillis()).toString());
        txtDataFim = new JTextField();
        cbFiltro = new JComboBox<>(new String[]{"Todos", "Ativos", "Finalizados"});

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Livro:"));
        form.add(cbLivro);
        form.add(new JLabel("Membro:"));
        form.add(cbMembro);
        form.add(new JLabel("Data inicio (aaaa-mm-dd):"));
        form.add(txtDataInicio);
        form.add(new JLabel("Data fim (opcional, aaaa-mm-dd):"));
        form.add(txtDataFim);

        JButton btnRealizar = new JButton("Realizar Emprestimo");
        JButton btnEncerrar = new JButton("Encerrar Emprestimo");
        JButton btnListar = new JButton("Listar Emprestimos");

        btnRealizar.addActionListener(e -> realizar());
        btnEncerrar.addActionListener(e -> encerrar());
        btnListar.addActionListener(e -> listar());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoes.add(btnRealizar);
        botoes.add(btnEncerrar);
        botoes.add(new JLabel("Mostrar:"));
        botoes.add(cbFiltro);
        botoes.add(btnListar);

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        modeloTabela = new DefaultTableModel(
                new String[]{"ID", "Livro", "Membro", "Inicio", "Fim"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        setLayout(new BorderLayout());
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    /** Preenche os dois JComboBox com os livros e membros cadastrados. */
    private void carregarCombos() {
        try {
            cbLivro.removeAllItems();
            for (Livro l : livroDAO.listar()) {
                cbLivro.addItem(l);
            }
            cbMembro.removeAllItems();
            for (Membro m : membroDAO.listar()) {
                cbMembro.addItem(m);
            }
        } catch (SQLException ex) {
            mostrarErro("Erro ao carregar livros e membros", ex);
        }
    }

    /** Botao "Realizar Emprestimo" -> INSERT. */
    private void realizar() {
        Livro livro = (Livro) cbLivro.getSelectedItem();
        Membro membro = (Membro) cbMembro.getSelectedItem();

        if (livro == null || membro == null) {
            JOptionPane.showMessageDialog(this,
                    "Cadastre ao menos um livro e um membro antes de fazer um emprestimo.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date inicio;
        try {
            inicio = Date.valueOf(txtDataInicio.getText().trim());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Data de inicio invalida. Use o formato aaaa-mm-dd.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // regra: um livro nao pode ter dois emprestimos ativos ao mesmo tempo
            if (emprestimoDAO.livroEstaEmprestado(livro.getId())) {
                JOptionPane.showMessageDialog(this, "Este livro ja esta emprestado e ainda nao foi devolvido.",
                        "Atencao", JOptionPane.WARNING_MESSAGE);
                return;
            }
            emprestimoDAO.realizar(livro.getId(), membro.getId(), inicio);
            JOptionPane.showMessageDialog(this, "Emprestimo realizado com sucesso!");
            txtDataInicio.setText(new Date(System.currentTimeMillis()).toString());
            txtDataFim.setText("");
            listar();
        } catch (SQLException ex) {
            mostrarErro("Erro ao realizar o emprestimo", ex);
        }
    }

    /**
     * Botao "Encerrar Emprestimo" -> UPDATE data_fim.
     * Usa o emprestimo selecionado na tabela. Se o campo "Data fim" estiver
     * vazio, usa a data de hoje.
     */
    private void encerrar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um emprestimo na tabela.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) modeloTabela.getValueAt(linha, 0);

        Date fim;
        String texto = txtDataFim.getText().trim();
        try {
            fim = texto.isEmpty() ? new Date(System.currentTimeMillis()) : Date.valueOf(texto);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Data fim invalida. Use o formato aaaa-mm-dd.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int alteradas = emprestimoDAO.encerrar(id, fim);
            if (alteradas == 0) {
                JOptionPane.showMessageDialog(this, "Este emprestimo ja estava encerrado.",
                        "Atencao", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Emprestimo encerrado com sucesso!");
                txtDataFim.setText("");
            }
            listar();
        } catch (SQLException ex) {
            mostrarErro("Erro ao encerrar o emprestimo", ex);
        }
    }

    /** Botao "Listar Emprestimos" -> SELECT com JOIN, respeitando o filtro escolhido. */
    private void listar() {
        int filtro = EmprestimoDAO.TODOS;
        if (cbFiltro.getSelectedIndex() == 1) {
            filtro = EmprestimoDAO.ATIVOS;
        } else if (cbFiltro.getSelectedIndex() == 2) {
            filtro = EmprestimoDAO.FINALIZADOS;
        }

        try {
            List<Emprestimo> lista = emprestimoDAO.listar(filtro);
            modeloTabela.setRowCount(0);
            for (Emprestimo e : lista) {
                modeloTabela.addRow(new Object[]{
                    e.getId(),
                    e.getTituloLivro(),
                    e.getNomeMembro(),
                    e.getDataInicio(),
                    e.isAtivo() ? "Em aberto" : e.getDataFim()
                });
            }
        } catch (SQLException ex) {
            mostrarErro("Erro ao listar os emprestimos", ex);
        }
    }

    private void mostrarErro(String mensagem, SQLException ex) {
        JOptionPane.showMessageDialog(this, mensagem + ":\n" + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
