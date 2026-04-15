package com.pos.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

        dashboard.addActionListener(e -> {
    tengah.removeAll();
    tengah.setBackground(new Color(245, 247, 250));

    // ================= TITLE =================
    JLabel title = new JLabel("Dashboard");
    title.setFont(new Font("SansSerif", Font.BOLD, 26));
    title.setBounds(30, 20, 300, 40);

    JLabel subtitle = new JLabel("Selamat datang di sistem Point of Sale Toko Buku");
    subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
    subtitle.setForeground(new Color(120, 120, 120));
    subtitle.setBounds(30, 60, 500, 20);

    // ================= CARD 1 =================
    JPanel cardStok = new JPanel();
    cardStok.setLayout(null);
    cardStok.setBackground(Color.WHITE);
    cardStok.setBounds(30, 110, 180, 110);
    cardStok.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

    JPanel top1 = new JPanel();
    top1.setBackground(new Color(52, 152, 219));
    top1.setBounds(0, 0, 180, 6);

    JLabel t1 = new JLabel("Total Buku");
    t1.setFont(new Font("SansSerif", Font.PLAIN, 12));
    t1.setForeground(Color.GRAY);
    t1.setBounds(10, 20, 160, 20);

    JLabel v1 = new JLabel("1,250");
    v1.setFont(new Font("SansSerif", Font.BOLD, 26));
    v1.setForeground(new Color(52, 152, 219));
    v1.setBounds(10, 50, 160, 40);

    cardStok.add(top1);
    cardStok.add(t1);
    cardStok.add(v1);

    // ================= CARD 2 =================
    JPanel cardKasir = new JPanel();
    cardKasir.setLayout(null);
    cardKasir.setBackground(Color.WHITE);
    cardKasir.setBounds(230, 110, 180, 110);
    cardKasir.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

    JPanel top2 = new JPanel();
    top2.setBackground(new Color(46, 204, 113));
    top2.setBounds(0, 0, 180, 6);

    JLabel t2 = new JLabel("Kasir Aktif");
    t2.setFont(new Font("SansSerif", Font.PLAIN, 12));
    t2.setForeground(Color.GRAY);
    t2.setBounds(10, 20, 160, 20);

    JLabel v2 = new JLabel("3");
    v2.setFont(new Font("SansSerif", Font.BOLD, 26));
    v2.setForeground(new Color(46, 204, 113));
    v2.setBounds(10, 50, 160, 40);

    cardKasir.add(top2);
    cardKasir.add(t2);
    cardKasir.add(v2);

    // ================= CARD 3 =================
    JPanel cardTransaksi = new JPanel();
    cardTransaksi.setLayout(null);
    cardTransaksi.setBackground(Color.WHITE);
    cardTransaksi.setBounds(430, 110, 180, 110);
    cardTransaksi.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

    JPanel top3 = new JPanel();
    top3.setBackground(new Color(155, 89, 182));
    top3.setBounds(0, 0, 180, 6);

    JLabel t3 = new JLabel("Transaksi");
    t3.setFont(new Font("SansSerif", Font.PLAIN, 12));
    t3.setForeground(Color.GRAY);
    t3.setBounds(10, 20, 160, 20);

    JLabel v3 = new JLabel("320");
    v3.setFont(new Font("SansSerif", Font.BOLD, 26));
    v3.setForeground(new Color(155, 89, 182));
    v3.setBounds(10, 50, 160, 40);

    cardTransaksi.add(top3);
    cardTransaksi.add(t3);
    cardTransaksi.add(v3);

    // ================= CARD 4 =================
    JPanel cardHabis = new JPanel();
    cardHabis.setLayout(null);
    cardHabis.setBackground(Color.WHITE);
    cardHabis.setBounds(30, 240, 180, 110);
    cardHabis.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

    JPanel top4 = new JPanel();
    top4.setBackground(new Color(231, 76, 60));
    top4.setBounds(0, 0, 180, 6);

    JLabel t4 = new JLabel("Stok Habis");
    t4.setFont(new Font("SansSerif", Font.PLAIN, 12));
    t4.setForeground(Color.GRAY);
    t4.setBounds(10, 20, 160, 20);

    JLabel v4 = new JLabel("12");
    v4.setFont(new Font("SansSerif", Font.BOLD, 26));
    v4.setForeground(new Color(231, 76, 60));
    v4.setBounds(10, 50, 160, 40);

    cardHabis.add(top4);
    cardHabis.add(t4);
    cardHabis.add(v4);


    JPanel cardPenjualan = new JPanel();
cardPenjualan.setLayout(null);
cardPenjualan.setBackground(Color.WHITE);
cardPenjualan.setBounds(230, 240, 180, 110);
cardPenjualan.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

JPanel top5 = new JPanel();
top5.setBackground(new Color(241, 196, 15)); // kuning emas
top5.setBounds(0, 0, 180, 6);

JLabel t5 = new JLabel("Total Penjualan");
t5.setFont(new Font("SansSerif", Font.PLAIN, 12));
t5.setForeground(Color.GRAY);
t5.setBounds(10, 20, 160, 20);

JLabel v5 = new JLabel("Rp 12.500.000");
v5.setFont(new Font("SansSerif", Font.BOLD, 22));
v5.setForeground(new Color(241, 196, 15));
v5.setBounds(10, 50, 170, 40);

cardPenjualan.add(top5);
cardPenjualan.add(t5);
cardPenjualan.add(v5);

    // ================= ADD ALL =================
    tengah.add(title);
    tengah.add(subtitle);
    tengah.add(cardStok);
    tengah.add(cardKasir);
    tengah.add(cardTransaksi);
    tengah.add(cardHabis);
    tengah.add(cardPenjualan);
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