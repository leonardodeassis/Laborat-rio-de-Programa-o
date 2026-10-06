package br.com.biblioteca;

import javax.swing.SwingUtilities;
import br.com.biblioteca.view.TelaPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
