package br.com.biblioteca.view;

import javax.swing.*;
import java.awt.*;

/**
 * Tela principal: todos os módulos são acessados por botões,
 * atendendo ao requisito de navegação pelo menu principal.
 */
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema de Biblioteca");
        setSize(620, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        construirTela();
    }

    private void construirTela() {
        JPanel painel = new JPanel(new BorderLayout(15, 15));
        painel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JLabel titulo = new JLabel("SISTEMA DE BIBLIOTECA", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel botoes = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton btnLivros = new JButton("Gerenciar Livros");
        JButton btnMembros = new JButton("Gerenciar Membros");
        JButton btnEmprestimos = new JButton("Realizar Empréstimo");
        JButton btnDevolucoes = new JButton("Registrar Devolução");
        JButton btnSair = new JButton("Sair");

        btnLivros.addActionListener(e -> new TelaLivros(this).setVisible(true));
        btnMembros.addActionListener(e -> new TelaMembros(this).setVisible(true));
        btnEmprestimos.addActionListener(e -> new TelaEmprestimo(this).setVisible(true));
        btnDevolucoes.addActionListener(e -> new TelaDevolucao(this).setVisible(true));
        btnSair.addActionListener(e -> System.exit(0));

        botoes.add(btnLivros);
        botoes.add(btnMembros);
        botoes.add(btnEmprestimos);
        botoes.add(btnDevolucoes);
        botoes.add(btnSair);

        painel.add(botoes, BorderLayout.CENTER);
        JLabel rodape = new JLabel("Dados armazenados em ArrayList durante a execução.", SwingConstants.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);

        add(painel);
    }
}
