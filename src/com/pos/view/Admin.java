package com.pos.view;

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
        dashboard.setBounds(25, 50, 150, 40);
        dashboard.setBackground(new Color(192, 57, 43));
        dashboard.setForeground(Color.WHITE);
        dashboard.setBorder(BorderFactory.createEmptyBorder());
        kiri.add(dashboard); 

        // tombol kasir
        JButton kasir = new JButton("Kasir");
        kasir.setBounds(25, 110, 150, 40);
        kasir.setBackground(new Color(192, 57, 43));
        kasir.setForeground(Color.WHITE);
        kasir.setFocusPainted(false);
        kasir.setBorder(BorderFactory.createEmptyBorder());
        kiri.add(kasir); 



        // tombol manajemen buku
        JButton manajemenBuku = new JButton("Manajemen Buku");
        manajemenBuku.setBounds(25, 170, 150, 40);
        manajemenBuku.setBackground(new Color(192, 57, 43));
        manajemenBuku.setForeground(Color.WHITE);
        manajemenBuku.setFocusPainted(false);
        manajemenBuku.setBorder(BorderFactory.createEmptyBorder());
        kiri.add(manajemenBuku);

        // tombol transaksi
        JButton transaksi = new JButton("Transaksi");
        transaksi.setBounds(25, 230, 150, 40);
        transaksi.setBackground(new Color(192, 57, 43));
        transaksi.setForeground(Color.WHITE);
        transaksi.setFocusPainted(false);
        transaksi.setBorder(BorderFactory.createEmptyBorder());
        kiri.add(transaksi);


        // tombol logut
        JButton logout = new JButton("logout");
        logout.setBounds(25, 290, 150, 40);
        logout.setBackground(new Color(192, 57, 43));
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);
        logout.setBorder(BorderFactory.createEmptyBorder());
        kiri.add(logout);


        setVisible(true);


        // aksi logout
        logout.addActionListener(e -> {
            new Signup();
            this.dispose();
          
        });

        // aksi dashboard

    dashboard.addActionListener(e ->{
       tengah.removeAll(); 
            
            JLabel title = new JLabel("Dashboard");
            title.setFont(new Font("SansSerif", Font.BOLD, 24));
            title.setBounds(30, 20, 300, 40);
            
            JLabel subtitle = new JLabel("Selamat datang di sistem Point of Sale Toko Buku");
            subtitle.setForeground(Color.ORANGE);
            subtitle.setBounds(30, 55, 400, 20);

            // Contoh Card Simple (Kotak statistik seperti di gambar)
            JPanel cardStok = new JPanel();
            cardStok.setBackground(Color.WHITE);
            cardStok.setBounds(30, 100, 200, 100);
            cardStok.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
            
            tengah.add(title);
            tengah.add(subtitle);
            tengah.add(cardStok);

            // PENTING: Refresh tampilan
            tengah.revalidate();
            tengah.repaint();
        });


        kasir.addActionListener(e ->{
            tengah.removeAll(); 
            
            JLabel title = new JLabel("Kasir");
            title.setFont(new Font("SansSerif", Font.BOLD, 24));
            title.setBounds(30, 20, 300, 40);
            
            JLabel subtitle = new JLabel("Pilih buku untuk ditambahkan ke keranjang");
            subtitle.setForeground(Color.ORANGE);
            subtitle.setBounds(30, 55, 400, 20);
             


            tengah.add(title);
            tengah.add(subtitle);
            

            // PENTING: Refresh tampilan
            tengah.revalidate();
            tengah.repaint();

        });

        manajemenBuku.addActionListener(e ->{
            tengah.removeAll(); 
            
            JLabel title = new JLabel("Manajemen Bukuu");
            title.setFont(new Font("SansSerif", Font.BOLD, 24));
            title.setBounds(30, 20, 300, 40);
            
            JLabel subtitle = new JLabel("Kelola data buku dalam inventori");
            subtitle.setForeground(Color.ORANGE);
            subtitle.setBounds(30, 55, 400, 20);
             


            tengah.add(title);
            tengah.add(subtitle);
            

            // PENTING: Refresh tampilan
            tengah.revalidate();
            tengah.repaint();

        }
        );


         transaksi.addActionListener(e ->{
            tengah.removeAll(); 
            
            JLabel title = new JLabel("Transaksi");
            title.setFont(new Font("SansSerif", Font.BOLD, 24));
            title.setBounds(30, 20, 300, 40);
            
            JLabel subtitle = new JLabel("Kelola Transaksi");
            subtitle.setForeground(Color.ORANGE);
            subtitle.setBounds(30, 55, 400, 20);
             


            tengah.add(title);
            tengah.add(subtitle);
            

            // PENTING: Refresh tampilan
            tengah.revalidate();
            tengah.repaint();

        }
        );


       
    }

    public static void main(String[] args) {
        new Admin();
    }
}