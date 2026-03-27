package tokobuku;

import java.awt.*;
import javax.swing.*;

public class FormDaftar extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;

    public FormDaftar() {
        setTitle("Daftar Akun Baru");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(30, 30, 30));

        // PANEL CARD
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBounds(75, 60, 350, 300);
        card.setBackground(new Color(50, 50, 50));
        card.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 2));
        add(card);

        // Judul: DAFTAR AKUN
        JLabel title = new JLabel("DAFTAR AKUN");
        title.setBounds(75, 10, 200, 40);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title);

        // Username
        JLabel userLabel = new JLabel("Username Baru");
        userLabel.setBounds(30, 70, 150, 20);
        userLabel.setForeground(Color.LIGHT_GRAY);
        card.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(30, 90, 280, 35);
        usernameField.setBackground(new Color(70, 70, 70));
        usernameField.setForeground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        card.add(usernameField);

        // Password
        JLabel passLabel = new JLabel("Password Baru");
        passLabel.setBounds(30, 140, 150, 20);
        passLabel.setForeground(Color.LIGHT_GRAY);
        card.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(30, 160, 280, 35);
        passwordField.setBackground(new Color(70, 70, 70));
        passwordField.setForeground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        card.add(passwordField);

        // Tombol Daftar
        JButton btnSimpan = new JButton("Simpan Akun");
        btnSimpan.setBounds(30, 210, 280, 35);
        btnSimpan.setBackground(new Color(46, 204, 113)); // Warna hijau biar beda
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setBorder(BorderFactory.createEmptyBorder());
        card.add(btnSimpan);

        // TOMBOL KEMBALI
        JButton btnKembali = new JButton("Kembali ke Login");
        btnKembali.setBounds(30, 255, 280, 30);
        btnKembali.setBackground(new Color(192, 57, 43)); // Warna merah
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setBorder(BorderFactory.createEmptyBorder());
        card.add(btnKembali);

        // Action Kembali
        btnKembali.addActionListener(e -> {
            new Signup(); // Buka kembali halaman utama (Signup)
            this.dispose(); // Tutup halaman daftar
        });

        // Action Simpan (Hanya simulasi)
        btnSimpan.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (user.isBlank() && pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username Dan Password Tidak Boleh Kosong");
            } else {
                JOptionPane.showMessageDialog(this, "Username: " + user + " Berhasil Terdaftar");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
    // Supaya JFrame muncul di thread Event Dispatch Thread
    javax.swing.SwingUtilities.invokeLater(() -> {
        new FormDaftar();
    });
}
}