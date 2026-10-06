package br.com.faculdade.sistemabancario;

import javax.swing.SwingUtilities;

/**
 * Ponto de entrada da aplicação.
 *
 * @author Leonardo
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Banco banco = new Banco();

            // Dados iniciais para facilitar a demonstração do trabalho.
            banco.cadastrarConta("1001", "Leonardo - Conta Exemplo", 1500.00);
            banco.cadastrarConta("1002", "Maria da Silva", 800.00);

            new TelaPrincipal(banco).setVisible(true);
        });
    }
}
