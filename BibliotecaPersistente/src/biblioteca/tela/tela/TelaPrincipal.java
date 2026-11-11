package biblioteca.tela;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Tela principal do sistema (menu).
 * Tem tres botoes e cada um abre a janela correspondente:
 *   Gerenciamento de Livros      -> TelaLivros
 *   Gerenciamento de Membros     -> TelaMembros
 *   Gerenciamento de Emprestimos -> TelaEmprestimos
 * Esta e a classe principal do projeto (contem o main).
 */
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        super("Biblioteca - Tela Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // fechar esta tela encerra o programa

        JButton btnLivros = new JButton("Gerenciamento de Livros");
        JButton btnMembros = new JButton("Gerenciamento de Membros");
        JButton btnEmprestimos = new JButton("Gerenciamento de Emprestimos");

        // Cada botao abre a respectiva janela
        btnLivros.addActionListener(e -> new TelaLivros().setVisible(true));
        btnMembros.addActionListener(e -> new TelaMembros().setVisible(true));
        btnEmprestimos.addActionListener(e -> new TelaEmprestimos().setVisible(true));

        JPanel painel = new JPanel(new GridLayout(3, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        painel.add(btnLivros);
        painel.add(btnMembros);
        painel.add(btnEmprestimos);

        add(painel);
        setSize(360, 240);
        setLocationRelativeTo(null);   // centraliza na tela
    }

    public static void main(String[] args) {
        // Swing deve ser iniciado na Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
