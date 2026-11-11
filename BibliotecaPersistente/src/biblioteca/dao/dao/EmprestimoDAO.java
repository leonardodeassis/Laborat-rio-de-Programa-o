package biblioteca.dao;

import biblioteca.modelo.Emprestimo;
import biblioteca.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DAO de Emprestimo: SQL da tabela "emprestimo" (inclui o JOIN da listagem). */
public class EmprestimoDAO {

    // Constantes para o filtro da listagem
    public static final int TODOS = 0;
    public static final int ATIVOS = 1;
    public static final int FINALIZADOS = 2;

    /** INSERT INTO emprestimo (livro_id, membro_id, data_inicio) VALUES (?, ?, ?) */
    public void realizar(int livroId, int membroId, Date dataInicio) throws SQLException {
        String sql = "INSERT INTO emprestimo (livro_id, membro_id, data_inicio) VALUES (?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, livroId);
            ps.setInt(2, membroId);
            ps.setDate(3, dataInicio);
            ps.executeUpdate();
        }
    }

    /**
     * UPDATE emprestimo SET data_fim = ? WHERE id = ?
     * Acrescentei "AND data_fim IS NULL" para nao sobrescrever a data de um
     * emprestimo que ja foi encerrado. Retorna quantas linhas foram alteradas.
     */
    public int encerrar(int id, Date dataFim) throws SQLException {
        String sql = "UPDATE emprestimo SET data_fim = ? WHERE id = ? AND data_fim IS NULL";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, dataFim);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }

    /** Verifica se o livro ja esta emprestado (existe emprestimo sem data_fim). */
    public boolean livroEstaEmprestado(int livroId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprestimo WHERE livro_id = ? AND data_fim IS NULL";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, livroId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    /** Lista emprestimos com JOIN para trazer o titulo do livro e o nome do membro. */
    public List<Emprestimo> listar(int filtro) throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();

        String sql = "SELECT e.id, e.livro_id, e.membro_id, l.titulo, m.nome, "
                   + "e.data_inicio, e.data_fim "
                   + "FROM emprestimo e "
                   + "INNER JOIN livro l ON l.id = e.livro_id "
                   + "INNER JOIN membro m ON m.id = e.membro_id ";
        if (filtro == ATIVOS) {
            sql += "WHERE e.data_fim IS NULL ";
        } else if (filtro == FINALIZADOS) {
            sql += "WHERE e.data_fim IS NOT NULL ";
        }
        sql += "ORDER BY e.id DESC";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Emprestimo e = new Emprestimo();
                e.setId(rs.getInt("id"));
                e.setLivroId(rs.getInt("livro_id"));
                e.setMembroId(rs.getInt("membro_id"));
                e.setTituloLivro(rs.getString("titulo"));
                e.setNomeMembro(rs.getString("nome"));
                e.setDataInicio(rs.getDate("data_inicio"));
                e.setDataFim(rs.getDate("data_fim"));   // null se ativo
                lista.add(e);
            }
        }
        return lista;
    }
}
