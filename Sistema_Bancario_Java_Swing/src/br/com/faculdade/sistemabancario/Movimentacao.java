package br.com.faculdade.sistemabancario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registra uma movimentação realizada em uma conta.
 *
 * @author Leonardo
 */
public class Movimentacao {
    private final LocalDateTime data;
    private final String tipo;
    private final double valor;
    private final String descricao;

    public Movimentacao(String tipo, double valor, String descricao) {
        this.data = LocalDateTime.now();
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return data.format(formato) + " | " + tipo +
                " | R$ " + String.format("%.2f", valor).replace('.', ',') +
                " | " + descricao;
    }
}
