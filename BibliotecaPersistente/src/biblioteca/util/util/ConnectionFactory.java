package biblioteca.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fabrica de conexoes com o MySQL.
 * Todos os DAOs chamam ConnectionFactory.getConnection() para abrir uma conexao,
 * assim usuario/senha ficam em um unico lugar.
 *
 * ATENCAO: ajuste USUARIO e SENHA conforme o MySQL instalado na sua maquina.
 */
public class ConnectionFactory {

    private static final String URL =
            "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";   // <-- ajustar
    private static final String SENHA = "leo123";     // <-- ajustar

    private ConnectionFactory() {
        // classe utilitaria, nao precisa ser instanciada
    }

    /** Abre uma nova conexao. Quem chamar deve fechar (try-with-resources). */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
