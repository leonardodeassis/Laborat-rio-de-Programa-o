package biblioteca.dao;

import biblioteca.modelo.Membro;
import biblioteca.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DAO de Membro: SQL da tabela "membro". Mesma ideia do LivroDAO. */
public class MembroDAO {

    public void inserir(Membro m) throws SQLException {
        String sql = "INSERT INTO membro (nome, matricula, contato) VALUES (?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNome());
            ps.setString(2, m.getMatricula());
            ps.setString(3, m.getContato());
            ps.executeUpdate();
        }
    }

    public void atualizar(Membro m) throws SQLException {
        String sql = "UPDATE membro SET nome = ?, matricula = ?, contato = ? WHERE id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNome());
            ps.setString(2, m.getMatricula());
            ps.setString(3, m.getContato());
            ps.setInt(4, m.getId());
            ps.executeUpdate();
        }
    }

    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM membro WHERE id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Membro> listar() throws SQLException {
        List<Membro> lista = new ArrayList<>();
        String sql = "SELECT id, nome, matricula, contato FROM membro ORDER BY nome";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Membro(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("matricula"),
                        rs.getString("contato")));
            }
        }
        return lista;
    }
}
