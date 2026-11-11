package biblioteca.tela;

import biblioteca.dao.LivroDAO;
import biblioteca.modelo.Livro;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Tela de gerenciamento de Livros (CRUD).
 *
 * Ligacao botao -> banco (sempre via LivroDAO):
 *   Inserir Livro   -> LivroDAO.inserir()   (INSERT)
 *   Editar Livro    -> LivroDAO.atualizar() (UPDATE)
 *   Remover Livro   -> LivroDAO.remover()   (DELETE)
 *   Listar          -> LivroDAO.listar()    (SELECT *) e popula a JTable
 *
 * Fluxo: ao clicar numa linha da tabela, os campos sao preenchidos e o id do
 * livro fica guardado em idSelecionado, para editar/remover.
 */
public class TelaLivros extends JFrame {

    private final LivroDAO dao = new LivroDAO();

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtAno;
    private JTextField txtIsbn;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    // id do livro selecionado na tabela (null = nenhum selecionado)
    private Integer idSelecionado = null;

    public TelaLivros() {
        super("Gerenciamento de Livros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   // fecha so esta janela
        montarTela();
        setSize(700, 480);
        setLocationRelativeTo(null);
        listar();   // ja mostra os livros ao abrir
    }

    /** Monta os componentes da tela (campos, botoes e tabela). */
    private void montarTela() {
        // ---- Formulario ----
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtAno = new JTextField();
        txtIsbn = new JTextField();

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Titulo:"));
        form.add(txtTitulo);
        form.add(new JLabel("Autor:"));
        form.add(txtAutor);
        form.add(new JLabel("Ano:"));
        form.add(txtAno);
        form.add(new JLabel("ISBN:"));
        form.add(txtIsbn);

        // ---- Botoes ----
        JButton btnInserir = new JButton("Inserir Livro");
        JButton btnEditar = new JButton("Editar Livro selecionado");
        JButton btnRemover = new JButton("Remover Livro selecionado");
        JButton btnListar = new JButton("Listar");

        btnInserir.addActionListener(e -> inserir());
        btnEditar.addActionListener(e -> editar());
        btnRemover.addActionListener(e -> remover());
        btnListar.addActionListener(e -> listar());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoes.add(btnInserir);
        botoes.add(btnEditar);
        botoes.add(btnRemover);
        botoes.add(btnListar);

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        // ---- Tabela (celulas nao editaveis) ----
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Titulo", "Autor", "Ano", "ISBN"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        // Quando o usuario clica numa linha, joga os dados nos campos
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                preencherCampos(tabela.getSelectedRow());
            }
        });

        setLayout(new BorderLayout());
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    /** Botao "Inserir Livro" -> INSERT. */
    private void inserir() {
        Livro livro = lerFormulario();
        if (livro == null) {
            return;   // validacao falhou (a mensagem ja foi mostrada)
        }
        try {
            dao.inserir(livro);
            JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
            limparCampos();
            listar();
        } catch (SQLException ex) {
            mostrarErro("Erro ao inserir o livro", ex);
        }
    }

    /** Botao "Editar Livro selecionado" -> UPDATE. */
    private void editar() {
        if (idSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um livro na tabela para editar.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Livro livro = lerFormulario();
        if (livro == null) {
            return;
        }
        livro.setId(idSelecionado);
        try {
            dao.atualizar(livro);
            JOptionPane.showMessageDialog(this, "Livro atualizado com sucesso!");
            limparCampos();
            listar();
        } catch (SQLException ex) {
            mostrarErro("Erro ao atualizar o livro", ex);
        }
    }

    /** Botao "Remover Livro selecionado" -> DELETE. */
    private void remover() {
        if (idSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um livro na tabela para remover.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this, "Deseja realmente remover este livro?",
                "Confirmacao", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.remover(idSelecionado);
            JOptionPane.showMessageDialog(this, "Livro removido com sucesso!");
            limparCampos();
            listar();
        } catch (SQLException ex) {
            // 1451 = erro de chave estrangeira (livro tem emprestimos registrados)
            if (ex.getErrorCode() == 1451) {
                JOptionPane.showMessageDialog(this,
                        "Nao e possivel remover: este livro possui emprestimos registrados.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                mostrarErro("Erro ao remover o livro", ex);
            }
        }
    }

    /** Botao "Listar" -> SELECT * e popula a JTable. */
    private void listar() {
        try {
            List<Livro> livros = dao.listar();
            modeloTabela.setRowCount(0);   // limpa a tabela antes de preencher
            for (Livro l : livros) {
                modeloTabela.addRow(new Object[]{l.getId(), l.getTitulo(), l.getAutor(), l.getAno(), l.getIsbn()});
            }
            idSelecionado = null;
        } catch (SQLException ex) {
            mostrarErro("Erro ao listar os livros", ex);
        }
    }

    /** Le os campos, valida e devolve um Livro (ou null se algo estiver invalido). */
    private Livro lerFormulario() {
        String titulo = txtTitulo.getText().trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O titulo e obrigatorio.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            txtTitulo.requestFocus();
            return null;
        }
        if (titulo.length() > 255) {
            JOptionPane.showMessageDialog(this, "O titulo deve ter no maximo 255 caracteres.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Integer ano = null;   // ano e opcional
        String textoAno = txtAno.getText().trim();
        if (!textoAno.isEmpty()) {
            try {
                ano = Integer.parseInt(textoAno);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O ano deve ser um numero inteiro.",
                        "Atencao", JOptionPane.WARNING_MESSAGE);
                txtAno.requestFocus();
                return null;
            }
        }

        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setAutor(txtAutor.getText().trim());
        livro.setAno(ano);
        livro.setIsbn(txtIsbn.getText().trim());
        return livro;
    }

    /** Copia a linha selecionada da tabela para os campos de texto. */
    private void preencherCampos(int linha) {
        idSelecionado = (Integer) modeloTabela.getValueAt(linha, 0);
        txtTitulo.setText(valorTexto(linha, 1));
        txtAutor.setText(valorTexto(linha, 2));
        txtAno.setText(valorTexto(linha, 3));
        txtIsbn.setText(valorTexto(linha, 4));
    }

    private String valorTexto(int linha, int coluna) {
        Object valor = modeloTabela.getValueAt(linha, coluna);
        return valor == null ? "" : valor.toString();
    }

    /** Limpa os campos do formulario apos inserir/alterar/remover. */
    private void limparCampos() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAno.setText("");
        txtIsbn.setText("");
        idSelecionado = null;
        tabela.clearSelection();
    }

    private void mostrarErro(String mensagem, SQLException ex) {
        JOptionPane.showMessageDialog(this, mensagem + ":\n" + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
