package br.com.biblioteca.view;

import br.com.biblioteca.model.BibliotecaDados;
import br.com.biblioteca.model.Membro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaMembros extends JFrame {
    private final JTextField txtNome = new JTextField();
    private final JTextField txtCpf = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtTelefone = new JTextField();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nome", "CPF", "E-mail", "Telefone"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);

    public TelaMembros(JFrame principal) {
        setTitle("Gerenciamento de Membros");
        setSize(850, 520);
        setLocationRelativeTo(principal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        construir();
        atualizarTabela();
    }

    private void construir() {
        JPanel campos = new JPanel(new GridLayout(4, 2, 8, 8));
        campos.setBorder(BorderFactory.createTitledBorder("Dados do membro"));
        campos.add(new JLabel("Nome:")); campos.add(txtNome);
        campos.add(new JLabel("CPF:")); campos.add(txtCpf);
        campos.add(new JLabel("E-mail:")); campos.add(txtEmail);
        campos.add(new JLabel("Telefone:")); campos.add(txtTelefone);

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
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do membro.");
            return;
        }
        Membro m = new Membro(BibliotecaDados.proximoIdMembro(), nome,
                txtCpf.getText().trim(), txtEmail.getText().trim(), txtTelefone.getText().trim());
        BibliotecaDados.getMembros().add(m);
        atualizarTabela();
        limpar();
        JOptionPane.showMessageDialog(this, "Membro cadastrado com sucesso!");
    }

    private void excluir() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um membro.");
            return;
        }
        int id = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
        Membro encontrado = null;
        for (Membro m : BibliotecaDados.getMembros()) {
            if (m.getId() == id) { encontrado = m; break; }
        }
        BibliotecaDados.getMembros().remove(encontrado);
        atualizarTabela();
    }

    private void limpar() {
        txtNome.setText(""); txtCpf.setText(""); txtEmail.setText(""); txtTelefone.setText("");
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);
        for (Membro m : BibliotecaDados.getMembros()) {
            modelo.addRow(new Object[]{m.getId(), m.getNome(), m.getCpf(), m.getEmail(), m.getTelefone()});
        }
    }
}
