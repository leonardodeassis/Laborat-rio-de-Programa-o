package br.com.faculdade.sistemabancario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Camada simples de regras do sistema bancário.
 * Mantém as contas em memória durante a execução do programa.
 *
 * @author Leonardo
 */
public class Banco {
    private final Map<String, Conta> contas = new HashMap<>();
    private final Map<String, List<Movimentacao>> extratos = new HashMap<>();

    public void cadastrarConta(String numero, String titular, double saldoInicial) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o número da conta.");
        }
        if (titular == null || titular.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do titular.");
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        if (contas.containsKey(numero)) {
            throw new IllegalArgumentException("Já existe uma conta com esse número.");
        }

        Conta conta = new Conta(numero.trim(), titular.trim(), saldoInicial);
        contas.put(conta.getNumero(), conta);
        extratos.put(conta.getNumero(), new ArrayList<>());

        if (saldoInicial > 0) {
            registrar(conta.getNumero(), new Movimentacao(
                    "DEPÓSITO", saldoInicial, "Saldo inicial da conta"));
        }
    }

    public Conta buscarConta(String numero) {
        if (numero == null) {
            return null;
        }
        return contas.get(numero.trim());
    }

    public List<Conta> listarContas() {
        return new ArrayList<>(contas.values());
    }

    public List<Movimentacao> getExtrato(String numero) {
        List<Movimentacao> lista = extratos.get(numero);
        if (lista == null) {
            throw new IllegalArgumentException("Conta não encontrada.");
        }
        return new ArrayList<>(lista);
    }

    public void depositar(String numero, double valor) {
        Conta conta = exigirConta(numero);
        conta.depositar(valor);
        registrar(numero, new Movimentacao("DEPÓSITO", valor, "Depósito realizado"));
    }

    public void sacar(String numero, double valor) {
        Conta conta = exigirConta(numero);
        conta.sacar(valor);
        registrar(numero, new Movimentacao("SAQUE", valor, "Saque realizado"));
    }

    public void transferir(String origem, String destino, double valor) {
        Conta contaOrigem = exigirConta(origem);
        Conta contaDestino = exigirConta(destino);

        if (contaOrigem.getNumero().equals(contaDestino.getNumero())) {
            throw new IllegalArgumentException("A conta de origem e destino devem ser diferentes.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser maior que zero.");
        }
        if (valor > contaOrigem.getSaldo()) {
            throw new IllegalArgumentException("Saldo insuficiente para a transferência.");
        }

        contaOrigem.sacar(valor);
        contaDestino.depositar(valor);

        registrar(origem, new Movimentacao(
                "TRANSFERÊNCIA - SAÍDA", valor,
                "Transferência para a conta " + destino));

        registrar(destino, new Movimentacao(
                "TRANSFERÊNCIA - ENTRADA", valor,
                "Transferência recebida da conta " + origem));
    }

    private Conta exigirConta(String numero) {
        Conta conta = buscarConta(numero);
        if (conta == null) {
            throw new IllegalArgumentException("Conta não encontrada: " + numero);
        }
        return conta;
    }

    private void registrar(String numero, Movimentacao movimentacao) {
        extratos.get(numero).add(movimentacao);
    }
}
