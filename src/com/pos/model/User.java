package com.pos.model;

public abstract class User {
    protected String idUser;
    protected String username;
    protected String password;

    public User(String idUser, String username, String password) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
    }

    // Getter & Setter
    public String getUsername() { return username; }
}
