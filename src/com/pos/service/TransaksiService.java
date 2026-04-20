package com.pos.service;

import com.pos.database.DatabaseManager;
import java.sql.*;

public class TransaksiService {

    // fitur 9: cek stok sebelum dimasukkan ke keranjang
    public static int getStokBuku(String isbn) {
        String sql = "SELECT stok FROM buku WHERE isbn = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stok");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // fitur 10 & 11: simpan Transaksi dan Potong Stok sekaligus
    public static boolean prosesPembayaran(String idKasir, double totalHarga, double bayar, String[][] items) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            // Matikan auto-commit agar kita bisa rollback jika ada error
            conn.setAutoCommit(false);

            // 1. Simpan ke tabel 'transaksi'
            String sqlTransaksi = "INSERT INTO transaksi (id_user, total_harga, bayar, kembalian, tanggal) VALUES (?, ?, ?, ?, NOW())";
            PreparedStatement pstmtT = conn.prepareStatement(sqlTransaksi, Statement.RETURN_GENERATED_KEYS);
            pstmtT.setString(1, idKasir);
            pstmtT.setDouble(2, totalHarga);
            pstmtT.setDouble(3, bayar);
            pstmtT.setDouble(4, bayar - totalHarga);
            pstmtT.executeUpdate();

            // Ambil ID transaksi yang baru saja dibuat
            ResultSet rs = pstmtT.getGeneratedKeys();
            int idTransaksi = 0;
            if (rs.next()) idTransaksi = rs.getInt(1);

            // 2. Loop semua item belanja
            String sqlItem = "INSERT INTO detail_transaksi (id_transaksi, isbn, qty, subtotal) VALUES (?, ?, ?, ?)";
            String sqlUpdateStok = "UPDATE buku SET stok = stok - ? WHERE isbn = ?";
            
            PreparedStatement pstmtI = conn.prepareStatement(sqlItem);
            PreparedStatement pstmtU = conn.prepareStatement(sqlUpdateStok);

            for (String[] item : items) {
                String isbn = item[0];
                int qty = Integer.parseInt(item[1]);
                double subtotal = Double.parseDouble(item[2]);

                // Simpan detail
                pstmtI.setInt(1, idTransaksi);
                pstmtI.setString(2, isbn);
                pstmtI.setInt(3, qty);
                pstmtI.setDouble(4, subtotal);
                pstmtI.executeUpdate();

                // FITUR 11: Kurangi Stok
                pstmtU.setInt(1, qty);
                pstmtU.setString(2, isbn);
                pstmtU.executeUpdate();
            }

            // Jika semua lancar, simpan permanen
            conn.commit();
            return true;

        } catch (SQLException e) {
            // Jika ada satu saja yang gagal (misal stok kurang atau koneksi mati), batalkan semua!
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}