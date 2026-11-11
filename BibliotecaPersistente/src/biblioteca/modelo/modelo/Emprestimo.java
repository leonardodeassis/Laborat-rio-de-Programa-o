package biblioteca.modelo;

import java.sql.Date;

/**
 * Classe modelo da tabela "emprestimo".
 * Alem dos campos da tabela, guarda tituloLivro e nomeMembro, que vem do JOIN
 * e servem apenas para exibir na JTable.
 */
public class Emprestimo {

    private int id;
    private int livroId;
    private int membroId;
    private Date dataInicio;
    private Date dataFim;      // null enquanto o emprestimo estiver ativo

    // campos vindos do JOIN (so para exibicao)
    private String tituloLivro;
    private String nomeMembro;

    public Emprestimo() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLivroId() { return livroId; }
    public void setLivroId(int livroId) { this.livroId = livroId; }

    public int getMembroId() { return membroId; }
    public void setMembroId(int membroId) { this.membroId = membroId; }

    public Date getDataInicio() { return dataInicio; }
    public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }

    public Date getDataFim() { return dataFim; }
    public void setDataFim(Date dataFim) { this.dataFim = dataFim; }

    public String getTituloLivro() { return tituloLivro; }
    public void setTituloLivro(String tituloLivro) { this.tituloLivro = tituloLivro; }

    public String getNomeMembro() { return nomeMembro; }
    public void setNomeMembro(String nomeMembro) { this.nomeMembro = nomeMembro; }

    /** Emprestimo ativo = ainda nao tem data de devolucao. */
    public boolean isAtivo() {
        return dataFim == null;
    }
}
