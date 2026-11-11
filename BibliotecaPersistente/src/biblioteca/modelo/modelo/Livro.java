package biblioteca.modelo;

/** Classe modelo da tabela "livro". Atributos privados + getters/setters (encapsulamento). */
public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private Integer ano;   // Integer porque a coluna pode ser NULL
    private String isbn;

    public Livro() {
    }

    public Livro(int id, String titulo, String autor, Integer ano, String isbn) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.isbn = isbn;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    /** Usado pelo JComboBox da tela de emprestimos: mostra "id - titulo". */
    @Override
    public String toString() {
        return id + " - " + titulo;
    }
}
