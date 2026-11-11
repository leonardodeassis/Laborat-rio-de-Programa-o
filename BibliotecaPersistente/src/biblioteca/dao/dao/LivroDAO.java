package biblioteca.dao;

import biblioteca.modelo.Livro;
import biblioteca.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) de Livro: concentra todo o SQL da tabela "livro".
 * A TelaLivros so chama estes metodos e nao conhece SQL.
 * Todos usam PreparedStatement (evita SQL Injection) e try-with-resources
 * (fecha Connection/PreparedStatement/ResultSet automaticamente).
 */
public class LivroDAO {

    /** INSERT de um novo livro. */
    public void inserir(Livro livro) throws SQLException {
        String sql = "INSERT INTO livro (titulo, autor, ano, isbn) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, livro);
            ps.executeUpdate();
        }
    }

    /** UPDATE de um livro existente (identificado pelo id). */
    public void atualizar(Livro livro) throws SQLException {
        String sql = "UPDATE livro SET titulo = ?, autor = ?, ano = ?, isbn = ? WHERE id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, livro);
            ps.setInt(5, livro.getId());
            ps.executeUpdate();
        }
    }

    /** DELETE por id. Falha (erro 1451) se o livro ja tiver emprestimos. */
    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM livro WHERE id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /** SELECT * FROM livro. Retorna a lista para popular a JTable / JComboBox. */
    public List<Livro> listar() throws SQLException {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, ano, isbn FROM livro ORDER BY titulo";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Livro l = new Livro();
                l.setId(rs.getInt("id"));
                l.setTitulo(rs.getString("titulo"));
                l.setAutor(rs.getString("autor"));
                int ano = rs.getInt("ano");
                l.setAno(rs.wasNull() ? null : ano);   // trata ano NULL no banco
                l.setIsbn(rs.getString("isbn"));
                lista.add(l);
            }
        }
        return lista;
    }

    // Preenche os 4 primeiros "?" (compartilhado entre inserir e atualizar)
    private void preencher(PreparedStatement ps, Livro livro) throws SQLException {
        ps.setString(1, livro.getTitulo());
        ps.setString(2, livro.getAutor());
        if (livro.getAno() == null) {
            ps.setNull(3, Types.INTEGER);
        } else {
            ps.setInt(3, livro.getAno());
        }
        ps.setString(4, livro.getIsbn());
    }
}
