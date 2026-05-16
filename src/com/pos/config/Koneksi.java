package com.pos.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    
    private static Connection koneksi;

    public static Connection getConnection() {
    try {
        if (koneksi == null || koneksi.isClosed()) {
            
            String url = "jdbc:sqlite:C:/TokoBuku_PBO/tokobuku.db";
            
            Class.forName("org.sqlite.JDBC");
            koneksi = DriverManager.getConnection(url);
            
            System.out.println("KONEKSI SQLITE BERHASIL!");
            System.out.println("Path: " + url);
        }
        return koneksi;
    } catch (Exception e) {
        System.err.println("KONEKSI GAGAL: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}
}