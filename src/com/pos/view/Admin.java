package com.pos.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Admin extends JFrame {

    JPanel sidebar, content;
    JTable table, tableTransaksi; // Menambahkan tableTransaksi
    
    Color primaryColor = new Color(41, 128, 185);
    Color sidebarColor = new Color(44, 62, 80);
    Color bgColor = new Color(236, 240, 241);
    Color accentColor = new Color(46, 204, 113);
    Color warningColor = new Color(230, 126, 34); // Warna baru untuk transaksi

    private Connection connectDB() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/tokobuku", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --- DATA FETCHING ---

    private DefaultTableModel getBukuData() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ISBN", "Judul", "Harga", "Kategori", "Stok"}, 0);
        try (Connection c = connectDB()) {
            ResultSet r = c.createStatement().executeQuery("SELECT * FROM buku");
            while (r.next()) {
                model.addRow(new Object[]{
                    r.getString("isbn"), r.getString("judul"),
                    "Rp " + String.format("%,d", r.getInt("harga")),
                    r.getString("kategori"), r.getInt("stok")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return model;
    }

    private DefaultTableModel getTransaksiData() {
        // Sesuai kolom: id_transaksi, tanggal, total_harga, metode_bayar, nominal_bayar, id_kasir
        DefaultTableModel model = new DefaultTableModel(new String[]{
            "ID Transaksi", "Tanggal", "Total Harga", "Metode", "Nominal Bayar", "ID Kasir"
        }, 0);
        try (Connection c = connectDB()) {
            ResultSet r = c.createStatement().executeQuery("SELECT * FROM transaksi ORDER BY tanggal DESC");
            while (r.next()) {
                model.addRow(new Object[]{
                    r.getString("id_transaksi"),
                    r.getTimestamp("tanggal"),
                    "Rp " + String.format("%,d", r.getInt("total_harga")),
                    r.getString("metode_bayar"),
                    "Rp " + String.format("%,d", r.getInt("nominal_bayar")),
                    r.getString("id_kasir")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return model;
    }

    private void tambahStok(String isbn, int jml) {
        try (Connection c = connectDB()) {
            c.createStatement().executeUpdate("UPDATE buku SET stok=stok+" + jml + " WHERE isbn='" + isbn + "'");
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- GUI SETUP ---

    public Admin() {
        setTitle("CHAQRIZEMY - Admin Panel");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // SIDEBAR
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 700));
        sidebar.setBackground(sidebarColor);
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        JLabel logo = new JLabel("CHAQRIZEMY");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);
        logo.setBorder(new EmptyBorder(20, 0, 30, 0));
        sidebar.add(logo);

        JButton dashBtn = createSidebarBtn("Dashboard");
        JButton bukuBtn = createSidebarBtn("Manajemen Buku");
        JButton transBtn = createSidebarBtn("Riwayat Transaksi"); // Tombol baru

        sidebar.add(dashBtn);
        sidebar.add(bukuBtn);
        sidebar.add(transBtn);

        // CONTENT
        content = new JPanel(new CardLayout());
        content.add(createDashboard(), "dash");
        content.add(createBukuPanel(), "buku");
        content.add(createTransaksiPanel(), "trans"); // Panel baru

        dashBtn.addActionListener(e -> switchPanel("dash"));
        bukuBtn.addActionListener(e -> {
            table.setModel(getBukuData());
            switchPanel("buku");
        });
        transBtn.addActionListener(e -> {
            tableTransaksi.setModel(getTransaksiData());
            switchPanel("trans");
        });

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton createSidebarBtn(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(200, 45));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setBackground(sidebarColor);
        b.setForeground(new Color(189, 195, 199));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMargin(new Insets(0, 20, 0, 0));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { b.setBackground(new Color(52, 73, 94)); b.setForeground(Color.WHITE); }
            public void mouseExited(java.awt.event.MouseEvent evt) { b.setBackground(sidebarColor); b.setForeground(new Color(189, 195, 199)); }
        });
        return b;
    }

    private void switchPanel(String name) {
        CardLayout cl = (CardLayout) content.getLayout();
        cl.show(content, name);
    }

    // --- PANELS ---

    private JPanel createDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Ringkasan Sistem");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        p.add(title, BorderLayout.NORTH);

        JPanel cardContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        cardContainer.setBackground(bgColor);

        cardContainer.add(createCard("Total Judul Buku", String.valueOf(getCount("COUNT(*)", "buku")), primaryColor));
        cardContainer.add(createCard("Total Transaksi", String.valueOf(getCount("COUNT(*)", "transaksi")), warningColor));
        cardContainer.add(createCard("Total Pendapatan", "Rp " + String.format("%,d", getCount("SUM(total_harga)", "transaksi")), accentColor));

        p.add(cardContainer, BorderLayout.CENTER);
        return p;
    }

    private JPanel createBukuPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        p.add(new JLabel("<html><span style='font-size:18px; font-weight:bold;'>Manajemen Inventaris</span></html>"), BorderLayout.NORTH);

        table = new JTable(getBukuData());
        styleTable(table);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controls.setBackground(bgColor);
        JTextField input = new JTextField();
        input.setPreferredSize(new Dimension(100, 35));
        JButton btn = new JButton("Update Stok");
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setBackground(primaryColor);
        btn.setForeground(Color.WHITE);

        btn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            String isbn = table.getValueAt(row, 0).toString();
            tambahStok(isbn, Integer.parseInt(input.getText()));
            table.setModel(getBukuData());
        });

        controls.add(new JLabel("Tambah Unit:"));
        controls.add(input);
        controls.add(btn);
        p.add(controls, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createTransaksiPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgColor);
        
        JLabel title = new JLabel("Laporan Riwayat Transaksi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(title, BorderLayout.WEST);

        JButton refreshBtn = new JButton("Refresh Data");
        refreshBtn.addActionListener(e -> tableTransaksi.setModel(getTransaksiData()));
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        p.add(headerPanel, BorderLayout.NORTH);

        // Table
        tableTransaksi = new JTable(getTransaksiData());
        styleTable(tableTransaksi);
        JScrollPane sp = new JScrollPane(tableTransaksi);
        p.add(sp, BorderLayout.CENTER);

        return p;
    }

    // --- HELPERS ---

    private void styleTable(JTable t) {
        t.setRowHeight(35);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setSelectionBackground(new Color(232, 244, 253));
        t.setGridColor(new Color(230, 230, 230));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.getTableHeader().setBackground(Color.WHITE);
    }

    private int getCount(String queryPart, String tableName) {
        try (Connection c = connectDB()) {
            ResultSet r = c.createStatement().executeQuery("SELECT " + queryPart + " AS res FROM " + tableName);
            if (r.next()) return r.getInt("res");
        } catch (Exception e) { }
        return 0;
    }

    private JPanel createCard(String title, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(280, 120));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, accent));
        JLabel t = new JLabel(title);
        t.setBorder(new EmptyBorder(15, 20, 0, 0));
        JLabel v = new JLabel(value);
        v.setBorder(new EmptyBorder(0, 20, 15, 0));
        v.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        new Admin();
    }
}