package br.com.faculdade.sistemabancario;

/**
 * Modelo de uma conta bancária.
 *
 * @author Leonardo
 */
public class Conta {
    private final String numero;
    private final String titular;
    private double saldo;

    public Conta(String numero, String titular, double saldoInicial) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    public String getNumero() {
        return numero;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    /**
     * Realiza um depósito. Valores menores ou iguais a zero não são aceitos.
     */
    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser maior que zero.");
        }
        saldo += valor;
    }

    /**
     * Realiza um saque somente quando há saldo suficiente.
     */
    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser maior que zero.");
        }
        if (valor > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        saldo -= valor;
    }

    @Override
    public String toString() {
        return numero + " - " + titular + " | Saldo: R$ " +
                String.format("%.2f", saldo).replace('.', ',');
    }
}
