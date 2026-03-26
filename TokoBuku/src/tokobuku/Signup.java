package tokobuku;

import java.awt.*;
import javax.swing.*;

public class Signup extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;

    public Signup() {
        setTitle("PoS BookStore");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Background (dark modern)
        getContentPane().setBackground(new Color(30, 30, 30));

        // PANEL CARD 
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBounds(75, 60, 350, 300);
        card.setBackground(new Color(50, 50, 50));
        card.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 2));
        add(card);

        // Title
        JLabel title = new JLabel("LOGIN");
        title.setBounds(120, 10, 150, 40);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        card.add(title);

        // Username
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

        // Password
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

        // Button Login
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(30, 240, 280, 35);
        loginBtn.setBackground(new Color(100, 100, 100));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBorder(BorderFactory.createEmptyBorder());
        card.add(loginBtn);

        JButton daftar = new JButton("Buat Akun");
        daftar.setBounds(30, 200, 280, 35); 
        daftar.setBackground(new Color(100, 100, 100)); 
        daftar.setForeground(Color.WHITE);
        daftar.setFocusPainted(false);
        daftar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        daftar.setBorder(BorderFactory.createEmptyBorder());
        card.add(daftar);

        daftar.addActionListener(e -> {
            new FormDaftar();
        });

        // Action Login
        loginBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (user.equals("admin") && pass.equals("admin123")) {
                JOptionPane.showMessageDialog(this, "Selamat Datang Admin");
            } else if (user.equals("kasir") && pass.equals("kasir123")) {
                JOptionPane.showMessageDialog(this, "Selamat Datang Kasir");
            } else {
                JOptionPane.showMessageDialog(this, "Username / Password salah!");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Signup();
        });
    }
}