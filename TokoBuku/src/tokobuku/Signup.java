package tokobuku;

import java.awt.*;
import javax.swing.*;

public class Signup extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;

    public Signup () {
        setTitle("PoS BookStore");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Background
        getContentPane().setBackground(new Color(30, 30, 30));

        // Title
        JLabel title = new JLabel("LOGIN");
        title.setBounds(160, 20, 100, 30);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title);

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(50, 70, 100, 20);
        userLabel.setForeground(Color.WHITE);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(50, 90, 300, 30);
        add(usernameField);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(50, 130, 100, 20);
        passLabel.setForeground(Color.WHITE);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(50, 150, 300, 30);
        add(passwordField);

        // Button Login
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(50, 200, 300, 35);
        loginBtn.setBackground(new Color(0, 120, 215));
        loginBtn.setForeground(Color.WHITE);
        add(loginBtn);

        // Action Login
        loginBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (user.equals("admin") && pass.equals("123")) {
                JOptionPane.showMessageDialog(this, "Login Berhasil!");
            } else {
                JOptionPane.showMessageDialog(this, "Username / Password salah!");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Signup().setVisible(true);
        });
    }
}