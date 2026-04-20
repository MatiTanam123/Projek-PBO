package com.pos.model;

public class Admin extends User {
    public Admin(String idUser, String username, String password) {
        super(idUser, username, password);
    }

    @Override
    public void tampilkanRole() {
        System.out.println("Login sebagai: Admin Utama");
    }
}