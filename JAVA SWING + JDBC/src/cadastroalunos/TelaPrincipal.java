package cadastroalunos;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TelaPrincipal extends JFrame {

    private JButton jBAlunos;
    private JButton jBCadastrarProfessor;
    private JButton jBListarProfessores;
    private JButton jBCadastrarCurso;
    private JButton jBListarCursos;

    public TelaPrincipal() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema Escola");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jBAlunos = new JButton("Alunos (Cadastrar e Listar)");
        jBCadastrarProfessor = new JButton("Cadastrar Professor");
        jBListarProfessores = new JButton("Listar Professores");
        jBCadastrarCurso = new JButton("Cadastrar Curso");
        jBListarCursos = new JButton("Listar Cursos");

        JPanel painel = new JPanel(new GridLayout(5, 1, 8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        painel.add(jBAlunos);
        painel.add(jBCadastrarProfessor);
        painel.add(jBListarProfessores);
        painel.add(jBCadastrarCurso);
        painel.add(jBListarCursos);
        add(painel);

        jBAlunos.addActionListener(evt -> new TelaAluno().setVisible(true));
        jBCadastrarProfessor.addActionListener(evt -> new TelaCadastroProfessor().setVisible(true));
        jBListarProfessores.addActionListener(evt -> new TelaListagemProfessor().setVisible(true));
        jBCadastrarCurso.addActionListener(evt -> new TelaCadastroCurso().setVisible(true));
        jBListarCursos.addActionListener(evt -> new TelaListagemCurso().setVisible(true));

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}
