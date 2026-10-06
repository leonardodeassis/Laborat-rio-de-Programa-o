package br.com.biblioteca.view;

import br.com.biblioteca.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class TelaDevolucao extends JFrame {
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Livro", "Membro", "Empréstimo", "Situação"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);

    public TelaDevolucao(JFrame principal) {
        setTitle("Devoluções");
        setSize(850, 500);
        setLocationRelativeTo(principal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        construir();
        atualizarTabela();
    }

    private void construir() {
        JButton devolver = new JButton("Registrar devolução");
        JButton atualizar = new JButton("Atualizar");
        JButton voltar = new JButton("Voltar");

        devolver.addActionListener(e -> devolver());
        atualizar.addActionListener(e -> atualizarTabela());
        voltar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel();
        botoes.add(devolver); botoes.add(atualizar); botoes.add(voltar);

        add(botoes, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void devolver() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo.");
            return;
        }
        int id = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
        for (Emprestimo e : BibliotecaDados.getEmprestimos()) {
            if (e.getId() == id) {
                if (e.isDevolvido()) {
                    JOptionPane.showMessageDialog(this, "Este empréstimo já foi devolvido.");
                    return;
                }
                e.setDevolvido(true);
                e.setDataDevolucao(LocalDate.now());
                e.getLivro().setDisponivel(true);
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Devolução registrada com sucesso!");
                return;
            }
        }
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);
        for (Emprestimo e : BibliotecaDados.getEmprestimos()) {
            String situacao = e.isDevolvido()
                    ? "Devolvido em " + e.getDataDevolucao()
                    : "Em aberto";
            modelo.addRow(new Object[]{e.getId(), e.getLivro().getTitulo(),
                    e.getMembro().getNome(), e.getDataEmprestimo(), situacao});
        }
    }
}
