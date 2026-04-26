package dao;

import util.DatabaseConnection;
import java.sql.*;

public class AdminDAO {

    // Cek login admin berdasarkan tabel admin di database
    public static boolean login(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true jika ada data
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // (Opsional) ambil data admin berdasarkan username
    public static String getAdminId(String username) {
        String sql = "SELECT id_admin FROM admin WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("id_admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}