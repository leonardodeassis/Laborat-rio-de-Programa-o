package cadastroalunos;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TelaListagemCurso extends JFrame {

    private JButton jBListar;
    private JTextArea jTxtArea;

    public TelaListagemCurso() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Listagem de Cursos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        jBListar = new JButton("Listar");
        jTxtArea = new JTextArea(14, 45);
        jTxtArea.setEditable(false);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(jBListar);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        centro.add(new JScrollPane(jTxtArea), BorderLayout.CENTER);

        add(painelBotoes, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        jBListar.addActionListener(evt -> jBListarActionPerformed(evt));

        pack();
        setLocationRelativeTo(null);
    }

    private void jBListarActionPerformed(java.awt.event.ActionEvent evt) {
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(TelaListagemCurso.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            return;
        }
        String sql = "SELECT * FROM curso";
        try {
            var stmt = con.prepareStatement(sql);
            var rs = stmt.executeQuery();
            jTxtArea.setText("");
            while (rs.next()) {
                jTxtArea.append(
                        rs.getInt("id") + " - Nome: "
                        + rs.getString("nome") + " - Carga horaria: "
                        + rs.getString("carga_horaria") + "\n"
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
