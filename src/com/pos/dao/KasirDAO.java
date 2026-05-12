package com.pos.dao;

import com.pos.config.Koneksi;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class KasirDAO {

    // Generate ID otomatis: KSR001, KSR002, dst.
    public static String generateId() {
        String sql = "SELECT id_kasir FROM kasir ORDER BY id_kasir DESC LIMIT 1";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String last = rs.getString("id_kasir"); // misal "KSR003"
                int num = Integer.parseInt(last.substring(3)) + 1;
                return String.format("KSR%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "KSR001"; // default jika tabel masih kosong
    }

    // Cek apakah username sudah dipakai
    public static boolean usernameExists(String username) {
        String sql = "SELECT id_kasir FROM kasir WHERE username = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tambah kasir baru
    public static boolean tambahKasir(String username, String password) {
        if (usernameExists(username)) return false;

        String sql = "INSERT INTO kasir (id_kasir, username, password) VALUES (?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, generateId());
            ps.setString(2, username);
            ps.setString(3, password);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ambil semua kasir untuk ditampilkan di tabel
    public static DefaultTableModel getAllKasir() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID Kasir", "Username"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        String sql = "SELECT id_kasir, username FROM kasir";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_kasir"),
                    rs.getString("username")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    // Hapus kasir berdasarkan ID
    public static boolean hapusKasir(String idKasir) {
        String sql = "DELETE FROM kasir WHERE id_kasir = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idKasir);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}