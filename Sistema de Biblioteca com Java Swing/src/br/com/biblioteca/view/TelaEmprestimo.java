package br.com.biblioteca.view;

import br.com.biblioteca.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaEmprestimo extends JFrame {
    private final JComboBox<Livro> cbLivro = new JComboBox<>();
    private final JComboBox<Membro> cbMembro = new JComboBox<>();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Livro", "Membro", "Data", "Situação"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);

    public TelaEmprestimo(JFrame principal) {
        setTitle("Empréstimos");
        setSize(850, 520);
        setLocationRelativeTo(principal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        construir();
        carregarCombos();
        atualizarTabela();
    }

    private void construir() {
        JPanel formulario = new JPanel(new GridLayout(2, 2, 8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder("Novo empréstimo"));
        formulario.add(new JLabel("Livro disponível:")); formulario.add(cbLivro);
        formulario.add(new JLabel("Membro:")); formulario.add(cbMembro);

        JButton emprestar = new JButton("Realizar empréstimo");
        JButton atualizar = new JButton("Atualizar");
        JButton voltar = new JButton("Voltar");
        emprestar.addActionListener(e -> emprestar());
        atualizar.addActionListener(e -> { carregarCombos(); atualizarTabela(); });
        voltar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel();
        botoes.add(emprestar); botoes.add(atualizar); botoes.add(voltar);

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.add(formulario, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void carregarCombos() {
        cbLivro.removeAllItems();
        for (Livro l : BibliotecaDados.getLivros()) if (l.isDisponivel()) cbLivro.addItem(l);
        cbMembro.removeAllItems();
        for (Membro m : BibliotecaDados.getMembros()) cbMembro.addItem(m);
    }

    private void emprestar() {
        Livro livro = (Livro) cbLivro.getSelectedItem();
        Membro membro = (Membro) cbMembro.getSelectedItem();
        if (livro == null || membro == null) {
            JOptionPane.showMessageDialog(this, "É necessário ter livro disponível e membro cadastrado.");
            return;
        }
        livro.setDisponivel(false);
        BibliotecaDados.getEmprestimos().add(
                new Emprestimo(BibliotecaDados.proximoIdEmprestimo(), livro, membro));
        atualizarTabela();
        carregarCombos();
        JOptionPane.showMessageDialog(this, "Empréstimo realizado com sucesso!");
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);
        for (Emprestimo e : BibliotecaDados.getEmprestimos()) {
            modelo.addRow(new Object[]{e.getId(), e.getLivro().getTitulo(), e.getMembro().getNome(),
                    e.getDataEmprestimo(), e.isDevolvido() ? "Devolvido" : "Em aberto"});
        }
    }
}
