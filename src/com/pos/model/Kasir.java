package com.pos.model;

public class Kasir extends User {
    
    // Constructor untuk membuat objek Kasir baru
    public Kasir(String idUser, String username, String password) {
        super(idUser, username, password);
    }

    /**
     * Metode ini opsional, tapi berguna jika nanti Jere ingin menampilkan 
     * nama kasir di Struk atau Header Dashboard dengan format tertentu.
     */
    @Override
    public String getUsername() {
        return super.getUsername().toUpperCase(); // Contoh: Nama kasir selalu huruf kapital
    }

    // Metode identitas khusus role8
    @Override
    public void tampilkanRole() {
        System.out.println("Login sebagai: Kasir Operasional");
    }

}