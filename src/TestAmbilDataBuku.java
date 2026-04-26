import util.DatabaseConnection;
import java.sql.*;

public class TestAmbilDataBuku {
    public static void main(String[] args) {
        String sql = "SELECT isbn, judul, harga, kategori, stok FROM buku";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("Daftar Buku dari Database Toko Buku:\n");
            System.out.println("ISBN\t\tJudul\t\t\t\tHarga\t\tStok");
            System.out.println("--------------------------------------------------------------");
            
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String judul = rs.getString("judul");
                double harga = rs.getDouble("harga");
                int stok = rs.getInt("stok");
                
                // Format output agar rapi
                System.out.printf("%-15s %-30s Rp%-12.2f %d\n", isbn, judul, harga, stok);
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat mengambil data buku: " + e.getMessage());
            e.printStackTrace();
        }
    }
}