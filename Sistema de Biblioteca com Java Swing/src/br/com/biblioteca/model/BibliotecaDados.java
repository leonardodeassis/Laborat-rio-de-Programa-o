package br.com.biblioteca.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe central que mantém os registros durante a execução.
 * Nesta etapa os dados são armazenados em ArrayList, conforme solicitado.
 */
public class BibliotecaDados {
    private static final List<Livro> livros = new ArrayList<>();
    private static final List<Membro> membros = new ArrayList<>();
    private static final List<Emprestimo> emprestimos = new ArrayList<>();

    public static List<Livro> getLivros() { return livros; }
    public static List<Membro> getMembros() { return membros; }
    public static List<Emprestimo> getEmprestimos() { return emprestimos; }

    public static int proximoIdMembro() {
        return membros.size() + 1;
    }

    public static int proximoIdEmprestimo() {
        return emprestimos.size() + 1;
    }

    static {
        livros.add(new Livro("9780001", "Java Básico", "Autor Exemplo", 2024));
        livros.add(new Livro("9780002", "Programação Orientada a Objetos", "Maria Silva", 2023));
        membros.add(new Membro(1, "João da Silva", "111.111.111-11", "joao@email.com", "(31) 99999-1111"));
    }
}
