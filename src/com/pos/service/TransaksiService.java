package com.pos.service;

import com.pos.config.Koneksi;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransaksiService {

    // Ambil stok buku (untuk validasi)
    public static int getStokBuku(String isbn) {
        String sql = "SELECT stok FROM buku WHERE isbn = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("stok");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Menyimpan transaksi beserta item belanja ke database.
     * @param idKasir       ID kasir (misal 'KSR001')
     * @param totalHarga    total transaksi
     * @param nominalBayar  uang yang dibayar pelanggan
     * @param metodeBayar   TUNAI / QRIS / DEBIT / KREDIT
     * @param items         array of { isbn, qty, subtotal } (subtotal sebagai string)
     * @return true jika sukses, false jika gagal (rollback otomatis)
     */
    public static boolean simpanTransaksi(String idKasir, double totalHarga, double nominalBayar, 
                                          String metodeBayar, String[][] items) {
        Connection conn = null;
        try {
            conn = Koneksi.getConnection();
            conn.setAutoCommit(false); // mulai transaksi database

            // 1. Generate ID transaksi (format: TRK+timestamp yyyyMMddHHmmss)
            String idTransaksi = "TRK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // 2. Insert ke tabel transaksi
            String sqlTrans = "INSERT INTO transaksi (id_transaksi, total_harga, metode_bayar, nominal_bayar, id_kasir) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psTrans = conn.prepareStatement(sqlTrans)) {
                psTrans.setString(1, idTransaksi);
                psTrans.setDouble(2, totalHarga);
                psTrans.setString(3, metodeBayar);
                psTrans.setDouble(4, nominalBayar);
                psTrans.setString(5, idKasir);
                psTrans.executeUpdate();
            }

            // 3. Insert ke item_belanja dan (opsional) update stok jika trigger tidak ada
            String sqlItem = "INSERT INTO item_belanja (id_item, id_transaksi, isbn, qty, subtotal) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                for (String[] item : items) {
                    String isbn = item[0];
                    int qty = Integer.parseInt(item[1]);
                    double subtotal = Double.parseDouble(item[2]);
                    
                    String idItem = "ITM" + System.currentTimeMillis() + (int)(Math.random() * 1000);
                    psItem.setString(1, idItem);
                    psItem.setString(2, idTransaksi);
                    psItem.setString(3, isbn);
                    psItem.setInt(4, qty);
                    psItem.setDouble(5, subtotal);
                    psItem.executeUpdate();
                }
            }

            // 4. Jika trigger kurangi_stok_setelah_item belum ada, lakukan update stok manual di sini.
            //    Karena database kamu belum menerapkan trigger, kita tambahkan manual:
            String sqlUpdateStok = "UPDATE buku SET stok = stok - ? WHERE isbn = ?";
            try (PreparedStatement psStok = conn.prepareStatement(sqlUpdateStok)) {
                for (String[] item : items) {
                    int qty = Integer.parseInt(item[1]);
                    String isbn = item[0];
                    psStok.setInt(1, qty);
                    psStok.setString(2, isbn);
                    psStok.executeUpdate();
                }
            }

            conn.commit(); // semua berhasil, simpan permanen
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}