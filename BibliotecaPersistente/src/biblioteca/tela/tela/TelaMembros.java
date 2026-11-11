package biblioteca.tela;

import biblioteca.dao.MembroDAO;
import biblioteca.modelo.Membro;
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
 * Tela de gerenciamento de Membros (CRUD).
 *
 * Ligacao botao -> banco (via MembroDAO):
 *   Cadastrar -> inserir() (INSERT)
 *   Editar    -> atualizar() (UPDATE)
 *   Remover   -> remover() (DELETE)
 *   Listar    -> listar() (SELECT *) e popula a JTable
 * Funciona do mesmo jeito da TelaLivros.
 */
public class TelaMembros extends JFrame {

    private final MembroDAO dao = new MembroDAO();

    private JTextField txtNome;
    private JTextField txtMatricula;
    private JTextField txtContato;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private Integer idSelecionado = null;

    public TelaMembros() {
        super("Gerenciamento de Membros");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        montarTela();
        setSize(650, 450);
        setLocationRelativeTo(null);
        listar();
    }

    private void montarTela() {
        txtNome = new JTextField();
        txtMatricula = new JTextField();
        txtContato = new JTextField();

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("Matricula:"));
        form.add(txtMatricula);
        form.add(new JLabel("Contato:"));
        form.add(txtContato);

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnListar = new JButton("Listar");

        btnCadastrar.addActionListener(e -> cadastrar());
        btnEditar.addActionListener(e -> editar());
        btnRemover.addActionListener(e -> remover());
        btnListar.addActionListener(e -> listar());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoes.add(btnCadastrar);
        botoes.add(btnEditar);
        botoes.add(btnRemover);
        botoes.add(btnListar);

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Matricula", "Contato"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                preencherCampos(tabela.getSelectedRow());
            }
        });

        setLayout(new BorderLayout());
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    /** Botao "Cadastrar" -> INSERT. */
    private void cadastrar() {
        Membro m = lerFormulario();
        if (m == null) {
            return;
        }
        try {
            dao.inserir(m);
            JOptionPane.showMessageDialog(this, "Membro cadastrado com sucesso!");
            limparCampos();
            listar();
        } catch (SQLException ex) {
            mostrarErro("Erro ao cadastrar o membro", ex);
        }
    }

    /** Botao "Editar" -> UPDATE. */
    private void editar() {
        if (idSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um membro na tabela para editar.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Membro m = lerFormulario();
        if (m == null) {
            return;
        }
        m.setId(idSelecionado);
        try {
            dao.atualizar(m);
            JOptionPane.showMessageDialog(this, "Membro atualizado com sucesso!");
            limparCampos();
            listar();
        } catch (SQLException ex) {
            mostrarErro("Erro ao atualizar o membro", ex);
        }
    }

    /** Botao "Remover" -> DELETE. */
    private void remover() {
        if (idSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um membro na tabela para remover.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this, "Deseja realmente remover este membro?",
                "Confirmacao", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.remover(idSelecionado);
            JOptionPane.showMessageDialog(this, "Membro removido com sucesso!");
            limparCampos();
            listar();
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1451) {   // chave estrangeira
                JOptionPane.showMessageDialog(this,
                        "Nao e possivel remover: este membro possui emprestimos registrados.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                mostrarErro("Erro ao remover o membro", ex);
            }
        }
    }

    /** Botao "Listar" -> SELECT * e popula a JTable. */
    private void listar() {
        try {
            List<Membro> membros = dao.listar();
            modeloTabela.setRowCount(0);
            for (Membro m : membros) {
                modeloTabela.addRow(new Object[]{m.getId(), m.getNome(), m.getMatricula(), m.getContato()});
            }
            idSelecionado = null;
        } catch (SQLException ex) {
            mostrarErro("Erro ao listar os membros", ex);
        }
    }

    /** Valida nome e matricula (obrigatorios) e monta o objeto Membro. */
    private Membro lerFormulario() {
        String nome = txtNome.getText().trim();
        String matricula = txtMatricula.getText().trim();

        if (nome.isEmpty() || matricula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e matricula sao obrigatorios.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (nome.length() > 200 || matricula.length() > 50 || txtContato.getText().trim().length() > 100) {
            JOptionPane.showMessageDialog(this,
                    "Tamanho maximo: nome 200, matricula 50 e contato 100 caracteres.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        Membro m = new Membro();
        m.setNome(nome);
        m.setMatricula(matricula);
        m.setContato(txtContato.getText().trim());
        return m;
    }

    private void preencherCampos(int linha) {
        idSelecionado = (Integer) modeloTabela.getValueAt(linha, 0);
        txtNome.setText(valorTexto(linha, 1));
        txtMatricula.setText(valorTexto(linha, 2));
        txtContato.setText(valorTexto(linha, 3));
    }

    private String valorTexto(int linha, int coluna) {
        Object valor = modeloTabela.getValueAt(linha, coluna);
        return valor == null ? "" : valor.toString();
    }

    private void limparCampos() {
        txtNome.setText("");
        txtMatricula.setText("");
        txtContato.setText("");
        idSelecionado = null;
        tabela.clearSelection();
    }

    private void mostrarErro(String mensagem, SQLException ex) {
        JOptionPane.showMessageDialog(this, mensagem + ":\n" + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
