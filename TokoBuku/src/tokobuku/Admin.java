package tokobuku;

import java.awt.*;
import javax.swing.*;

public class Admin extends JFrame {

    // JTextField dashboardField;
    // JTextField kasir_posField;
    // JTextField manajamen_bukuField;
    // JTextField laporanField;

    JPanel kiri;
    JPanel tengah;

    public Admin() {
        setTitle("Halaman Admin");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // PANEL KIRI
        kiri = new JPanel();
        kiri.setLayout(null);
        kiri.setBounds(0, 0, 200, 400);
        kiri.setBackground(new Color(50, 50, 50));
        kiri.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        add(kiri);

        // PANEL TENGAH
        tengah = new JPanel();
        tengah.setLayout(null);
        tengah.setBounds(200, 0, 400, 400);
        tengah.setBackground(new Color(30, 30, 30));
        add(tengah);


        // buat button 

        JButton dashboard = new JButton("Dashboard");
        dashboard.setBounds(100, 100 , 100 , 100);
        dashboard.setBackground(new Color(192, 57, 43));
        dashboard.setForeground(Color.MAGENTA);
        dashboard.setFocusPainted(false);
        dashboard.setBorder(BorderFactory.createEmptyBorder());

        setVisible(true);
    }

    public static void main(String[] args) {
        new Admin();
    }
}