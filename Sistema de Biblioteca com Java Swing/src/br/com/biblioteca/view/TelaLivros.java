package br.com.biblioteca.view;

import br.com.biblioteca.model.BibliotecaDados;
import br.com.biblioteca.model.Livro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaLivros extends JFrame {
    private final JFrame principal;
    private final JTextField txtIsbn = new JTextField();
    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtAutor = new JTextField();
    private final JTextField txtAno = new JTextField();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ISBN", "Título", "Autor", "Ano", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);

    public TelaLivros(JFrame principal) {
        this.principal = principal;
        setTitle("Gerenciamento de Livros");
        setSize(800, 520);
        setLocationRelativeTo(principal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        construir();
        atualizarTabela();
    }

    private void construir() {
        JPanel campos = new JPanel(new GridLayout(4, 2, 8, 8));
        campos.setBorder(BorderFactory.createTitledBorder("Dados do livro"));
        campos.add(new JLabel("ISBN:")); campos.add(txtIsbn);
        campos.add(new JLabel("Título:")); campos.add(txtTitulo);
        campos.add(new JLabel("Autor:")); campos.add(txtAutor);
        campos.add(new JLabel("Ano:")); campos.add(txtAno);

        JButton cadastrar = new JButton("Cadastrar");
        JButton excluir = new JButton("Excluir selecionado");
        JButton limpar = new JButton("Limpar");
        JButton voltar = new JButton("Voltar");

        cadastrar.addActionListener(e -> cadastrar());
        excluir.addActionListener(e -> excluir());
        limpar.addActionListener(e -> limpar());
        voltar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel();
        botoes.add(cadastrar); botoes.add(excluir); botoes.add(limpar); botoes.add(voltar);

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.add(campos, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void cadastrar() {
        try {
            String isbn = txtIsbn.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty()) {
                throw new IllegalArgumentException("Preencha ISBN, título e autor.");
            }
            int ano = Integer.parseInt(txtAno.getText().trim());
            BibliotecaDados.getLivros().add(new Livro(isbn, titulo, autor, ano));
            atualizarTabela();
            limpar();
            JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ano deve ser um número inteiro.", "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluir() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um livro.");
            return;
        }
        String isbn = modelo.getValueAt(linha, 0).toString();
        Livro encontrado = null;
        for (Livro l : BibliotecaDados.getLivros()) {
            if (l.getIsbn().equals(isbn)) { encontrado = l; break; }
        }
        if (encontrado != null && !encontrado.isDisponivel()) {
            JOptionPane.showMessageDialog(this, "Não é possível excluir um livro emprestado.");
            return;
        }
        BibliotecaDados.getLivros().remove(encontrado);
        atualizarTabela();
    }

    private void limpar() {
        txtIsbn.setText(""); txtTitulo.setText(""); txtAutor.setText(""); txtAno.setText("");
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);
        for (Livro l : BibliotecaDados.getLivros()) {
            modelo.addRow(new Object[]{l.getIsbn(), l.getTitulo(), l.getAutor(), l.getAno(),
                    l.isDisponivel() ? "Disponível" : "Emprestado"});
        }
    }
}
