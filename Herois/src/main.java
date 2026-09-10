import java.util.ArrayList;
import java.util.List;

class Heroi {
    private String nome;
    private String classe;
    private int nivel;
    private int pontosVida;

    public Heroi(String nome, String classe, int nivel, int pontosVida) {
        this.nome = nome;
        this.classe = classe;
        this.nivel = nivel;
        this.pontosVida = pontosVida;
    }

    public String getNome() {
        return nome;
    }

    public String getClasse() {
        return classe;
    }

    public int getNivel() {
        return nivel;
    }

    public int getPontosVida() {
        return pontosVida;
    }

    public void atacar(String alvo) {
        System.out.println("-> " + nome + " (" + classe + ") atacou " + alvo + " causando dano!");
    }

    public void subirNivel() {
        this.nivel++;
        this.pontosVida += 25;
        System.out.println("-> " + nome + " subiu para o nivel " + nivel + "! HP aumentado para " + pontosVida + ".");
    }

    @Override
    public String toString() {
        return "Heroi: " + nome + " | Classe: " + classe + " | Nivel: " + nivel + " | HP: " + pontosVida;
    }
}

public class main {
    public static void main(String[] args) {
        System.out.println(" SISTEMA DE HEROIS DO JOGO ");

        List<Heroi> ligaDaJustica = new ArrayList<>();

        Heroi h1 = new Heroi("Batman", "Estrategista", 8, 110);
        Heroi h2 = new Heroi("Superman", "Alienigena", 12, 200);
        Heroi h3 = new Heroi("Mulher Maravilha", "Guerreira", 10, 150);
        Heroi h4 = new Heroi("Flash", "Velocista", 7, 90);

        ligaDaJustica.add(h1);
        ligaDaJustica.add(h2);
        ligaDaJustica.add(h3);
        ligaDaJustica.add(h4);

        System.out.println(" INTEGRANTES DA LIGA DA JUSTICA ");
        for (Heroi heroi : ligaDaJustica) {
            System.out.println(heroi);
        }
        System.out.println();

        System.out.println(" SIMULACAO DE COMBATE ");
        h1.atacar("Coringa");
        h2.atacar("Lex Luthor");
        h3.atacar("Ares");
        h4.atacar("Espirito Reverso");
        System.out.println();

        System.out.println(" EVOLUCAO DE HEROI ");
        h1.subirNivel();
        System.out.println();

        System.out.println(" STATUS ATUALIZADO DA EQUIPE ");
        for (Heroi heroi : ligaDaJustica) {
            System.out.println(heroi);
        }
        System.out.println("\n");
    }
}