// ini buat ngetes logika di suatu class doang, jalan apa engga. might delete later
import com.pos.database.DatabaseManager;
import java.sql.Connection;

public class TestApp {
    public static void main(String[] args) {
        System.out.println("--- Mengetes Koneksi Database ---");
        
        try {
            // Memanggil method getConnection dari DatabaseManager
            Connection conn = DatabaseManager.getConnection();
            
            if (conn != null) {
                System.out.println("SELAMAT! Java berhasil konek ke database 'tokobuku'.");
                conn.close(); // Jangan lupa ditutup setelah tes
            }
        } catch (Exception e) {
            System.err.println("Gagal konek: " + e.getMessage());
            System.out.println("\nCoba cek 3 hal ini:");
            System.out.println("1. Apakah Laragon sudah di-Start All?");
            System.out.println("2. Apakah password di DatabaseManager sudah 'caca227'?");
            System.out.println("3. Apakah mysql-connector-java.jar sudah ada di folder lib?");
        }
    }
}