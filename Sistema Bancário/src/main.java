import java.util.InputMismatchException;
import java.util.Scanner;

class ContaBancaria {
    private String banco;
    private String agencia;
    private String numeroConta;
    private String tipoConta;
    private double saldo;
    private boolean ativa;

    public ContaBancaria(String banco, String agencia, String numeroConta, String tipoConta) {
        this.banco = banco;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.tipoConta = tipoConta;
        this.saldo = 0.0;
        this.ativa = true;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public double getSaldo() {
        return saldo;
    }

    public void creditar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
            System.out.println("-> Deposito de R$ " + valor + " realizado com sucesso!");
        } else {
            System.out.println("-> Erro: O valor do deposito deve ser maior que zero.");
        }
    }

    public void debitar(double valor) {
        if (valor <= 0) {
            System.out.println("-> Erro: O valor do saque deve ser maior que zero.");
        } else if (valor > this.saldo) {
            System.out.println("-> Erro: Saldo insuficiente. Saldo atual: R$ " + this.saldo);
        } else {
            this.saldo -= valor;
            System.out.println("-> Saque de R$ " + valor + " realizado com sucesso!");
        }
    }

    public void consultarSaldo() {
        System.out.println("\n--- EXTRATO DA CONTA ---");
        System.out.println("Banco: " + banco + " | Agencia: " + agencia + " | Conta: " + numeroConta);
        System.out.println("Tipo de Conta: " + tipoConta);
        System.out.println("Saldo Atual: R$ " + saldo);
    }

    public void encerrarConta() {
        if (this.saldo > 0) {
            System.out.println("-> Atencao: Voce ainda possui R$ " + this.saldo + " na conta. Realize o saque do saldo antes de encerrar.");
        } else {
            this.ativa = false;
            System.out.println("-> Conta encerrada com sucesso. Obrigado por utilizar nossos servicos!");
        }
    }
}

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("      SISTEMA BANCARIO - ABERTURA DE CONTA       ");

        System.out.print("Digite o nome do Banco: ");
        String banco = scanner.nextLine();

        System.out.print("Digite a Agencia: ");
        String agencia = scanner.nextLine();

        System.out.print("Digite o Numero da Conta: ");
        String numeroConta = scanner.nextLine();

        System.out.print("Digite o Tipo de Conta (Corrente/Poupanca): ");
        String tipoConta = scanner.nextLine();

        ContaBancaria conta = new ContaBancaria(banco, agencia, numeroConta, tipoConta);
        System.out.println("\n-> Conta criada com sucesso!");

        while (conta.isAtiva()) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1 - Creditar (Depositar)");
            System.out.println("2 - Debitar (Sacar)");
            System.out.println("3 - Consultar Saldo");
            System.out.println("4 - Encerrar Conta");
            System.out.print("Escolha uma opcao: ");

            int opcao = 0;

            try {
                opcao = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("-> Erro: Entrada invalida. Digite apenas numeros inteiros para selecionar uma opcao.");
                scanner.nextLine(); 
            }

            switch (opcao) {
                case 1:
                    System.out.print("Digite o valor para creditar: ");
                    try {
                        double valorCredito = scanner.nextDouble();
                        conta.creditar(valorCredito);
                    } catch (InputMismatchException e) {
                        System.out.println("-> Erro: Valor invalido. Digite um numero valido.");
                        scanner.nextLine();
                    }
                    break;

                case 2:
                    System.out.print("Digite o valor para debitar: ");
                    try {
                        double valorDebito = scanner.nextDouble();
                        conta.debitar(valorDebito);
                    } catch (InputMismatchException e) {
                        System.out.println("-> Erro: Valor invalido. Digite um numero valido.");
                        scanner.nextLine();
                    }
                    break;

                case 3:
                    conta.consultarSaldo();
                    break;

                case 4:
                    conta.encerrarConta();
                    break;

                default:
                    System.out.println("-> Opcao invalida. Por favor, escolha uma opcao de 1 a 4.");
                    break;
            }
        }

        scanner.close();
    }
}