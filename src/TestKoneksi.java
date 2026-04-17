import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestKoneksi {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Koneksi ke database BERHASIL!");
            } else {
                System.out.println("Koneksi ke database GAGAL.");
            }
        } catch (SQLException e) {
            System.err.println("Error koneksi: " + e.getMessage());
        }
    }
}