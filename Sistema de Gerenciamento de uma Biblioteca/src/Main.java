import java.util.ArrayList;
import java.util.List;

class Livro {
    private String titulo;
    private String autor;
    private boolean disponivel;

    public Livro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
        this.disponivel = true;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        return "Livro: \"" + titulo + "\" | Autor: " + autor + " | Status: " + (disponivel ? "Disponivel" : "Emprestado");
    }
}

class Membro {
    private String nome;
    private int idMembro;

    public Membro(String nome, int idMembro) {
        this.nome = nome;
        this.idMembro = idMembro;
    }

    public String getNome() {
        return nome;
    }

    public int getIdMembro() {
        return idMembro;
    }

    @Override
    public String toString() {
        return "Membro: " + nome + " (ID: " + idMembro + ")";
    }
}

class Emprestimo {
    private Livro livro;
    private Membro membro;

    public Emprestimo(Livro livro, Membro membro) {
        this.livro = livro;
        this.membro = membro;
        this.livro.setDisponivel(false);
    }

    public void registrarDevolucao() {
        this.livro.setDisponivel(true);
        System.out.println("-> Devolucao efetuada: \"" + livro.getTitulo() + "\" foi devolvido por " + membro.getNome() + ".");
    }

    public void exibirDetalhes() {
        System.out.println("-> Emprestimo Registrado: \"" + livro.getTitulo() + "\" para " + membro.getNome() + " (ID: " + membro.getIdMembro() + ")");
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println(" SISTEMA DE GESTAO DE BIBLIOTECA ");
        String autorJK = "J.K. Rowling";

        List<Livro> acervo = new ArrayList<>();
        Livro l1 = new Livro("Harry Potter e a Pedra Filosofal", autorJK);
        Livro l2 = new Livro("Harry Potter e a Camara Secreta", autorJK);
        Livro l3 = new Livro("Harry Potter e o Prisioneiro de Azkaban", autorJK);
        Livro l4 = new Livro("Harry Potter e o Calice de Fogo", autorJK);

        acervo.add(l1);
        acervo.add(l2);
        acervo.add(l3);
        acervo.add(l4);

        Membro m1 = new Membro("Leonardo Assis", 202401);
        Membro m2 = new Membro("Luan Francisco", 202402);

        System.out.println(" ACERVO DISPONIVEL ");
        for (Livro livro : acervo) {
            System.out.println(livro);
        }
        System.out.println();

        System.out.println(" REGISTRO DE EMPRESTIMOS ");
        
        Emprestimo emp1 = null;
        if (l1.isDisponivel()) {
            emp1 = new Emprestimo(l1, m1);
            emp1.exibirDetalhes();
        }

        if (l3.isDisponivel()) {
            Emprestimo emp2 = new Emprestimo(l3, m2);
            emp2.exibirDetalhes();
        }
        System.out.println();

        System.out.println(" STATUS DO ACERVO APOS EMPRESTIMOS ");
        for (Livro livro : acervo) {
            System.out.println(livro);
        }
        System.out.println();

        System.out.println(" REGISTRO DE DEVOLUCOES ");
        if (emp1 != null) {
            emp1.registrarDevolucao();
        }
        System.out.println();

        System.out.println(" STATUS FINAL DO ACERVO ");
        for (Livro livro : acervo) {
            System.out.println(livro);
        }
        System.out.println("\n");
    }
}