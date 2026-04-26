package com.pos.view;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.pos.config.Koneksi;

public class Signup extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;

    public Signup() {
        setTitle("PoS BookStore");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // BACKGROUND
        getContentPane().setBackground(new Color(30, 30, 30));

        // CARD PANEL
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBounds(75, 60, 350, 300);
        card.setBackground(new Color(50, 50, 50));
        card.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 2));
        add(card);

        // TITLE
        JLabel title = new JLabel("LOGIN");
        title.setBounds(120, 10, 150, 40);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        card.add(title);

        // USERNAME
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(30, 70, 100, 20);
        userLabel.setForeground(Color.LIGHT_GRAY);
        card.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(30, 90, 280, 35);
        usernameField.setBackground(new Color(70, 70, 70));
        usernameField.setForeground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        card.add(usernameField);

        // PASSWORD
        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(30, 140, 100, 20);
        passLabel.setForeground(Color.LIGHT_GRAY);
        card.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(30, 160, 280, 35);
        passwordField.setBackground(new Color(70, 70, 70));
        passwordField.setForeground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        card.add(passwordField);

        // BUTTON LOGIN
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(30, 240, 280, 35);
        loginBtn.setBackground(new Color(100, 100, 100));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBorder(BorderFactory.createEmptyBorder());
        card.add(loginBtn);

        // ACTION LOGIN DATABASE
        loginBtn.addActionListener(e -> login());

        setVisible(true);
    }

    void login() {
    String user = usernameField.getText().trim();
    String pass = new String(passwordField.getPassword()).trim();

    if (user.isEmpty() || pass.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Username dan Password wajib diisi!");
        return;
    }

    try (Connection conn = Koneksi.getConnection()) {
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal!");
            return;
        }

        // Cek tabel admin
        String sqlAdmin = "SELECT id_admin FROM admin WHERE username=? AND password=?";
        PreparedStatement pstAdmin = conn.prepareStatement(sqlAdmin);
        pstAdmin.setString(1, user);
        pstAdmin.setString(2, pass);
        ResultSet rsAdmin = pstAdmin.executeQuery();

        if (rsAdmin.next()) {
            // Admin berhasil login
            JOptionPane.showMessageDialog(this, "Login sebagai Admin");
            new Admin();
            dispose();
            return;
        }
        rsAdmin.close();
        pstAdmin.close();

        // Cek tabel kasir
        String sqlKasir = "SELECT id_kasir, username FROM kasir WHERE username=? AND password=?";
        PreparedStatement pstKasir = conn.prepareStatement(sqlKasir);
        pstKasir.setString(1, user);
        pstKasir.setString(2, pass);
        ResultSet rsKasir = pstKasir.executeQuery();

        if (rsKasir.next()) {
            String idKasir = rsKasir.getString("id_kasir");
            String usernameKasir = rsKasir.getString("username");
            JOptionPane.showMessageDialog(this, "Login sebagai Kasir: " + usernameKasir);
            new Kasir(idKasir, usernameKasir);
            dispose();
            return;
        }
        rsKasir.close();
        pstKasir.close();

        // Jika tidak ada yang cocok
        JOptionPane.showMessageDialog(this, "Username / Password salah!");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        e.printStackTrace();
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Signup());
    }
}