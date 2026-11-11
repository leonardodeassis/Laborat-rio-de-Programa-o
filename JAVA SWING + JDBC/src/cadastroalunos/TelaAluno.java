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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TelaAluno extends JFrame {

    private JTextField jTxtNome;
    private JTextField jTxtEmail;
    private JButton jBCadastrar;
    private JButton jBListar;
    private JTextArea jTxtArea;

    public TelaAluno() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Cadastro de Alunos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        jTxtNome = new JTextField(25);
        jTxtEmail = new JTextField(25);
        jBCadastrar = new JButton("Salvar");
        jBListar = new JButton("Listar");
        jTxtArea = new JTextArea(12, 40);
        jTxtArea.setEditable(false);

        JPanel painelCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        painelCampos.add(new JLabel("Nome:"));
        painelCampos.add(jTxtNome);
        painelCampos.add(new JLabel("E-mail:"));
        painelCampos.add(jTxtEmail);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(jBCadastrar);
        painelBotoes.add(jBListar);

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        topo.add(painelCampos, BorderLayout.CENTER);
        topo.add(painelBotoes, BorderLayout.SOUTH);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        centro.add(new JScrollPane(jTxtArea), BorderLayout.CENTER);

        add(topo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        jBCadastrar.addActionListener(evt -> jBCadastrarActionPerformed(evt));
        jBListar.addActionListener(evt -> jBListarActionPerformed(evt));

        pack();
        setLocationRelativeTo(null);
    }

    private void jBCadastrarActionPerformed(java.awt.event.ActionEvent evt) {
        if (jTxtNome.getText().trim().isEmpty() || jTxtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha o nome e o e-mail.");
            return;
        }
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(TelaAluno.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            return;
        }
        String sql = "INSERT INTO aluno (nome, email) VALUES (?, ?)";
        try {
            var stmt = con.prepareStatement(sql);
            stmt.setString(1, jTxtNome.getText().trim());
            stmt.setString(2, jTxtEmail.getText().trim());
            stmt.executeUpdate();
            stmt.close();
            con.close();
            JOptionPane.showMessageDialog(null, "Aluno cadastrado!");
            jTxtNome.setText("");
            jTxtEmail.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    private void jBListarActionPerformed(java.awt.event.ActionEvent evt) {
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(TelaAluno.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            return;
        }
        String sql = "SELECT * FROM aluno";
        try {
            var stmt = con.prepareStatement(sql);
            var rs = stmt.executeQuery();
            jTxtArea.setText("");
            while (rs.next()) {
                jTxtArea.append(
                        rs.getInt("id") + " - Nome: "
                        + rs.getString("nome") + " - Email: "
                        + rs.getString("email") + "\n"
                );
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }
}
