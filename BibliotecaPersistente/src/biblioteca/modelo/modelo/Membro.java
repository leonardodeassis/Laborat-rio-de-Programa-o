package biblioteca.modelo;

/** Classe modelo da tabela "membro". */
public class Membro {

    private int id;
    private String nome;
    private String matricula;
    private String contato;

    public Membro() {
    }

    public Membro(int id, String nome, String matricula, String contato) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.contato = contato;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    /** Usado pelo JComboBox da tela de emprestimos: mostra "id - nome". */
    @Override
    public String toString() {
        return id + " - " + nome;
    }
}
