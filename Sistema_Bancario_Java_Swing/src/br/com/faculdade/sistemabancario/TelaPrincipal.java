package br.com.faculdade.sistemabancario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Interface gráfica principal do Sistema Bancário.
 *
 * O projeto usa Java Swing e separa as responsabilidades:
 * - Conta.java: representa uma conta.
 * - Movimentacao.java: representa o histórico.
 * - Banco.java: contém as regras de negócio.
 * - TelaPrincipal.java: interface e interação com o usuário.
 *
 * @author Leonardo
 */
public class TelaPrincipal extends JFrame {

    private final Banco banco;
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private final DefaultTableModel modeloContas = new DefaultTableModel(
            new Object[]{"Número", "Titular", "Saldo"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTable tabelaContas;
    private JTextField txtNumero;
    private JTextField txtTitular;
    private JTextField txtSaldoInicial;

    private JComboBox<String> cbDeposito;
    private JTextField txtValorDeposito;

    private JComboBox<String> cbSaque;
    private JTextField txtValorSaque;

    private JComboBox<String> cbOrigem;
    private JComboBox<String> cbDestino;
    private JTextField txtValorTransferencia;

    private JComboBox<String> cbExtrato;
    private JList<String> listaExtrato;
    private JLabel lblSaldoExtrato;

    public TelaPrincipal(Banco banco) {
        this.banco = banco;
        configurarJanela();
        criarInterface();
        atualizarComponentes();
    }

    private void configurarJanela() {
        setTitle("Sistema Bancário - Java Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setMinimumSize(new Dimension(850, 580));
        setLocationRelativeTo(null);
    }

    private void criarInterface() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel titulo = new JLabel("SISTEMA BANCÁRIO");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        titulo.setForeground(new Color(25, 75, 120));

        JLabel subtitulo = new JLabel("Atividade Prática 03 - Java Swing | Aluno: Leonardo");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.add(titulo, BorderLayout.NORTH);
        cabecalho.add(subtitulo, BorderLayout.SOUTH);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Contas", criarPainelContas());
        abas.addTab("Depósito", criarPainelDeposito());
        abas.addTab("Saque", criarPainelSaque());
        abas.addTab("Transferência", criarPainelTransferencia());
        abas.addTab("Extrato", criarPainelExtrato());

        raiz.add(cabecalho, BorderLayout.NORTH);
        raiz.add(abas, BorderLayout.CENTER);

        setContentPane(raiz);
    }

    private JPanel criarPainelContas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Cadastrar nova conta"));

        txtNumero = new JTextField(14);
        txtTitular = new JTextField(25);
        txtSaldoInicial = new JTextField(12);

        adicionarCampo(formulario, "Número da conta:", txtNumero, 0);
        adicionarCampo(formulario, "Nome do titular:", txtTitular, 1);
        adicionarCampo(formulario, "Saldo inicial:", txtSaldoInicial, 2);

        JButton btnCadastrar = new JButton("Cadastrar conta");
        btnCadastrar.addActionListener(e -> cadastrarConta());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        formulario.add(btnCadastrar, gbc);

        tabelaContas = new JTable(modeloContas);
        tabelaContas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaContas.setRowHeight(25);

        painel.add(formulario, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaContas), BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelDeposito() {
        JPanel painel = criarPainelOperacao("Realizar depósito");

        cbDeposito = new JComboBox<>();
        txtValorDeposito = new JTextField(15);

        adicionarCampo(painel, "Conta:", cbDeposito, 0);
        adicionarCampo(painel, "Valor:", txtValorDeposito, 1);

        JButton btn = new JButton("Confirmar depósito");
        btn.addActionListener(e -> realizarDeposito());

        adicionarBotao(painel, btn, 2);
        return painel;
    }

    private JPanel criarPainelSaque() {
        JPanel painel = criarPainelOperacao("Realizar saque");

        cbSaque = new JComboBox<>();
        txtValorSaque = new JTextField(15);

        adicionarCampo(painel, "Conta:", cbSaque, 0);
        adicionarCampo(painel, "Valor:", txtValorSaque, 1);

        JButton btn = new JButton("Confirmar saque");
        btn.addActionListener(e -> realizarSaque());

        adicionarBotao(painel, btn, 2);
        return painel;
    }

    private JPanel criarPainelTransferencia() {
        JPanel painel = criarPainelOperacao("Transferir entre contas");

        cbOrigem = new JComboBox<>();
        cbDestino = new JComboBox<>();
        txtValorTransferencia = new JTextField(15);

        adicionarCampo(painel, "Conta de origem:", cbOrigem, 0);
        adicionarCampo(painel, "Conta de destino:", cbDestino, 1);
        adicionarCampo(painel, "Valor:", txtValorTransferencia, 2);

        JButton btn = new JButton("Confirmar transferência");
        btn.addActionListener(e -> realizarTransferencia());

        adicionarBotao(painel, btn, 3);
        return painel;
    }

    private JPanel criarPainelExtrato() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        JPanel topo = new JPanel();
        cbExtrato = new JComboBox<>();
        lblSaldoExtrato = new JLabel("Saldo: R$ 0,00");
        lblSaldoExtrato.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton btnConsultar = new JButton("Consultar extrato");
        btnConsultar.addActionListener(e -> atualizarExtrato());

        topo.add(new JLabel("Conta:"));
        topo.add(cbExtrato);
        topo.add(btnConsultar);
        topo.add(lblSaldoExtrato);

        listaExtrato = new JList<>(new DefaultListModel<>());
        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(listaExtrato), BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelOperacao(String titulo) {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(titulo));
        return painel;
    }

    private void adicionarCampo(JPanel painel, String rotulo, java.awt.Component campo, int linha) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = linha;
        painel.add(new JLabel(rotulo), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        painel.add(campo, gbc);
    }

    private void adicionarBotao(JPanel painel, JButton botao, int linha) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 8, 8);
        painel.add(botao, gbc);
    }

    private double lerValor(JTextField campo) {
        String texto = campo.getText().trim().replace(",", ".");
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Informe um valor.");
        }
        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Digite um valor numérico válido.");
        }
    }

    private void cadastrarConta() {
        try {
            double saldo = lerValor(txtSaldoInicial);
            banco.cadastrarConta(txtNumero.getText(), txtTitular.getText(), saldo);

            JOptionPane.showMessageDialog(this,
                    "Conta cadastrada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            txtNumero.setText("");
            txtTitular.setText("");
            txtSaldoInicial.setText("");
            atualizarComponentes();

        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void realizarDeposito() {
        try {
            String conta = (String) cbDeposito.getSelectedItem();
            if (conta == null) {
                throw new IllegalArgumentException("Cadastre pelo menos uma conta.");
            }

            double valor = lerValor(txtValorDeposito);
            banco.depositar(extrairNumero(conta), valor);

            txtValorDeposito.setText("");
            JOptionPane.showMessageDialog(this, "Depósito realizado com sucesso!");
            atualizarComponentes();
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void realizarSaque() {
        try {
            String conta = (String) cbSaque.getSelectedItem();
            if (conta == null) {
                throw new IllegalArgumentException("Cadastre pelo menos uma conta.");
            }

            double valor = lerValor(txtValorSaque);
            banco.sacar(extrairNumero(conta), valor);

            txtValorSaque.setText("");
            JOptionPane.showMessageDialog(this, "Saque realizado com sucesso!");
            atualizarComponentes();
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void realizarTransferencia() {
        try {
            String origem = (String) cbOrigem.getSelectedItem();
            String destino = (String) cbDestino.getSelectedItem();

            if (origem == null || destino == null) {
                throw new IllegalArgumentException("Cadastre pelo menos duas contas.");
            }

            double valor = lerValor(txtValorTransferencia);

            banco.transferir(extrairNumero(origem), extrairNumero(destino), valor);

            txtValorTransferencia.setText("");
            JOptionPane.showMessageDialog(this, "Transferência realizada com sucesso!");
            atualizarComponentes();
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void atualizarExtrato() {
        try {
            String item = (String) cbExtrato.getSelectedItem();
            if (item == null) {
                throw new IllegalArgumentException("Cadastre uma conta primeiro.");
            }

            String numero = extrairNumero(item);
            Conta conta = banco.buscarConta(numero);

            DefaultListModel<String> modelo = new DefaultListModel<>();
            for (Movimentacao mov : banco.getExtrato(numero)) {
                modelo.addElement(mov.toString());
            }

            if (modelo.isEmpty()) {
                modelo.addElement("Nenhuma movimentação registrada.");
            }

            listaExtrato.setModel(modelo);
            lblSaldoExtrato.setText("Saldo: " + moeda.format(conta.getSaldo()));

        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void atualizarComponentes() {
        modeloContas.setRowCount(0);

        cbDeposito.removeAllItems();
        cbSaque.removeAllItems();
        cbOrigem.removeAllItems();
        cbDestino.removeAllItems();
        cbExtrato.removeAllItems();

        for (Conta conta : banco.listarContas()) {
            modeloContas.addRow(new Object[]{
                conta.getNumero(),
                conta.getTitular(),
                moeda.format(conta.getSaldo())
            });

            String item = conta.getNumero() + " - " + conta.getTitular();
            cbDeposito.addItem(item);
            cbSaque.addItem(item);
            cbOrigem.addItem(item);
            cbDestino.addItem(item);
            cbExtrato.addItem(item);
        }
    }

    private String extrairNumero(String item) {
        return item.substring(0, item.indexOf(" - "));
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Atenção",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
