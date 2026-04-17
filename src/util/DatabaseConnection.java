package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Informasi koneksi ke database MySQL di Laragon
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/tokobuku";
    private static final String USERNAME = "root"; // Username default Laragon
    private static final String PASSWORD = "";     // Password default Laragon kosong

    public static Connection getConnection() throws SQLException {
        try {
            // 1. Memuat driver JDBC (opsional untuk Java versi baru, tetapi tetap disarankan)
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2. Membuat koneksi ke database menggunakan DriverManager
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            // 3. Melempar exception jika driver tidak ditemukan
            throw new SQLException("Driver JDBC MySQL tidak ditemukan!", e);
        }
    }
}