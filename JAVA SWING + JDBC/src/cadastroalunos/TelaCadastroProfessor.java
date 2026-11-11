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

public class TelaCadastroProfessor extends JFrame {

    private JTextField jTxtNome;
    private JTextField jTxtDisciplina;
    private JButton jBInserir;

    public TelaCadastroProfessor() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Cadastro de Professores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        jTxtNome = new JTextField(25);
        jTxtDisciplina = new JTextField(25);
        jBInserir = new JButton("Inserir");

        JPanel painelCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        painelCampos.add(new JLabel("Nome:"));
        painelCampos.add(jTxtNome);
        painelCampos.add(new JLabel("Disciplina:"));
        painelCampos.add(jTxtDisciplina);

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
        String valor = jTxtDisciplina.getText().trim();
        if (nome.isEmpty() || valor.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            return;
        }
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(TelaCadastroProfessor.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            return;
        }
        String sql = "INSERT INTO professor (nome, disciplina) VALUES (?, ?)";
        try {
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, valor);
            stmt.executeUpdate();
            stmt.close();
            con.close();
            JOptionPane.showMessageDialog(null, "Professor cadastrado!");
            jTxtNome.setText("");
            jTxtDisciplina.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }
}
