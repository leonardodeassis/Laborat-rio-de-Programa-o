package cadastroalunos;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TelaCadastroCurso extends JFrame {

    private JTextField jTxtNome;
    private JTextField jTxtCargaHoraria;
    private JButton jBInserir;

    public TelaCadastroCurso() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Cadastro de Cursos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        jTxtNome = new JTextField(25);
        jTxtCargaHoraria = new JTextField(25);
        jBInserir = new JButton("Inserir");

        JPanel painelCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        painelCampos.add(new JLabel("Nome:"));
        painelCampos.add(jTxtNome);
        painelCampos.add(new JLabel("Carga horaria:"));
        painelCampos.add(jTxtCargaHoraria);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(jBInserir);

        add(painelCampos, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        jBInserir.addActionListener(evt -> jBInserirActionPerformed(evt));

        pack();
        setLocationRelativeTo(null);
    }

    private void jBInserirActionPerformed(java.awt.event.ActionEvent evt) {
        String nome = jTxtNome.getText().trim();
        String valor = jTxtCargaHoraria.getText().trim();
        if (nome.isEmpty() || valor.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            return;
        }
        int numero;
        try {
            numero = Integer.parseInt(valor);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "A carga horaria deve ser um numero inteiro.");
            return;
        }
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(TelaCadastroCurso.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            return;
        }
        String sql = "INSERT INTO curso (nome, carga_horaria) VALUES (?, ?)";
        try {
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setInt(2, numero);
            stmt.executeUpdate();
            stmt.close();
            con.close();
            JOptionPane.showMessageDialog(null, "Curso cadastrado!");
            jTxtNome.setText("");
            jTxtCargaHoraria.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }
}
