package com.pos.view;

import com.pos.service.TransaksiService;
import com.pos.config.Koneksi;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Kasir extends JFrame {

    private JTextField txtCari, txtQty, txtBayar;
    private JComboBox<String> cbMetodeBayar;
    private JTable tableKeranjang;
    private DefaultTableModel modelKeranjang;
    private JLabel lblTotal;
    private String idKasir; // ID kasir yang login (misal 'KSR001')
    private String usernameKasir;

    // Struktur data untuk keranjang: simpan isbn, judul, harga, qty, subtotal
    private List<KeranjangItem> keranjang = new ArrayList<>();

    public Kasir(String idKasir, String usernameKasir) {
        this.idKasir = idKasir;
        this.usernameKasir = usernameKasir;
        initUI();
        loadDataAwal();
    }

    private void initUI() {
        setTitle("CHAQRIZEMY - Kasir | " + usernameKasir);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel Utama
        JPanel main = new JPanel(new GridLayout(1, 2, 10, 10));
        main.setBorder(new EmptyBorder(10, 10, 10, 10));
        main.setBackground(new Color(236, 240, 241));

        // ===== PANEL KIRI : Pencarian & Keranjang =====
        JPanel panelKiri = new JPanel(new BorderLayout(5, 5));
        panelKiri.setBackground(Color.WHITE);

        // Form pencarian
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCari.add(new JLabel("Cari Buku (ISBN / Judul):"));
        txtCari = new JTextField(20);
        panelCari.add(txtCari);
        JButton btnCari = new JButton("Cari");
        panelCari.add(btnCari);

        // Tabel hasil pencarian
        String[] kolomHasil = {"ISBN", "Judul", "Harga", "Stok"};
        DefaultTableModel modelHasil = new DefaultTableModel(kolomHasil, 0);
        JTable tableHasil = new JTable(modelHasil);
        tableHasil.setRowHeight(30);
        JScrollPane scrollHasil = new JScrollPane(tableHasil);
        scrollHasil.setPreferredSize(new Dimension(0, 200));

        // Input qty dan tombol tambah
        JPanel panelAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAdd.add(new JLabel("Jumlah:"));
        txtQty = new JTextField(5);
        panelAdd.add(txtQty);
        JButton btnTambah = new JButton("Tambah ke Keranjang");
        panelAdd.add(btnTambah);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelCari, BorderLayout.NORTH);
        panelAtas.add(scrollHasil, BorderLayout.CENTER);
        panelAtas.add(panelAdd, BorderLayout.SOUTH);

        // Tabel keranjang belanja
        String[] kolomKeranjang = {"ISBN", "Judul", "Harga", "Qty", "Subtotal"};
        modelKeranjang = new DefaultTableModel(kolomKeranjang, 0);
        tableKeranjang = new JTable(modelKeranjang);
        tableKeranjang.setRowHeight(30);
        JScrollPane scrollKeranjang = new JScrollPane(tableKeranjang);
        
        // Panel untuk tombol edit/hapus keranjang
        JPanel panelActionKeranjang = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnEdit = new JButton("Edit Jumlah");
        JButton btnHapus = new JButton("Hapus Item");
        panelActionKeranjang.add(btnEdit);
        panelActionKeranjang.add(btnHapus);
        
        JPanel panelKeranjang = new JPanel(new BorderLayout());
        panelKeranjang.add(new JLabel("Keranjang Belanja"), BorderLayout.NORTH);
        panelKeranjang.add(scrollKeranjang, BorderLayout.CENTER);
        panelKeranjang.add(panelActionKeranjang, BorderLayout.SOUTH);

        panelKiri.add(panelAtas, BorderLayout.NORTH);
        panelKiri.add(panelKeranjang, BorderLayout.CENTER);

        // ===== PANEL KANAN : Pembayaran =====
        JPanel panelKanank = new JPanel(new BorderLayout(5, 5));
        panelKanank.setBackground(Color.WHITE);
        panelKanank.setBorder(new EmptyBorder(20, 20, 20, 20));

        lblTotal = new JLabel("Total: Rp 0", SwingConstants.CENTER);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTotal.setForeground(new Color(41, 128, 185));
        
        JPanel panelBayar = new JPanel(new GridLayout(4, 2, 10, 10));
        panelBayar.setBorder(new EmptyBorder(20, 0, 0, 0));
        panelBayar.add(new JLabel("Metode Bayar:"));
        cbMetodeBayar = new JComboBox<>(new String[]{"TUNAI", "QRIS", "DEBIT", "KREDIT"});
        panelBayar.add(cbMetodeBayar);
        panelBayar.add(new JLabel("Nominal Bayar:"));
        txtBayar = new JTextField(15);
        panelBayar.add(txtBayar);
        panelBayar.add(new JLabel("Kembalian:"));
        JLabel lblKembalian = new JLabel("Rp 0");
        lblKembalian.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelBayar.add(lblKembalian);

        JButton btnHitung = new JButton("Hitung Kembalian");
        JButton btnBayar = new JButton("PROSES BAYAR");
        btnBayar.setBackground(new Color(46, 204, 113));
        btnBayar.setForeground(Color.WHITE);
        btnBayar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JPanel panelTombol = new JPanel(new FlowLayout());
        panelTombol.add(btnHitung);
        panelTombol.add(btnBayar);

        panelKanank.add(lblTotal, BorderLayout.NORTH);
        panelKanank.add(panelBayar, BorderLayout.CENTER);
        panelKanank.add(panelTombol, BorderLayout.SOUTH);

        main.add(panelKiri);
        main.add(panelKanank);
        add(main, BorderLayout.CENTER);

        // ===== LOGOUT =====
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Logout");
        panelTop.add(btnLogout);
        add(panelTop, BorderLayout.NORTH);

        // ===== EVENT =====
        btnCari.addActionListener(e -> cariBuku(modelHasil, txtCari.getText()));
        btnTambah.addActionListener(e -> tambahKeKeranjang(tableHasil));
        btnEdit.addActionListener(e -> editJumlahKeranjang());
        btnHapus.addActionListener(e -> hapusItemKeranjang());
        btnHitung.addActionListener(e -> hitungKembalian(lblKembalian));
        btnBayar.addActionListener(e -> prosesBayar(lblKembalian));
        btnLogout.addActionListener(e -> {
            new Signup();
            dispose();
        });

        setVisible(true);
    }

    private void loadDataAwal() {
        // Bisa kosong, atau memuat daftar buku terbaru (opsional)
    }

    private void cariBuku(DefaultTableModel model, String keyword) {
        model.setRowCount(0);
        if (keyword.trim().isEmpty()) return;
        String sql = "SELECT isbn, judul, harga, stok FROM buku WHERE isbn LIKE ? OR judul LIKE ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("isbn"),
                    rs.getString("judul"),
                    "Rp " + String.format("%,d", rs.getInt("harga")),
                    rs.getInt("stok")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void tambahKeKeranjang(JTable tableHasil) {
        int selected = tableHasil.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku terlebih dahulu!");
            return;
        }
        String isbn = (String) tableHasil.getValueAt(selected, 0);
        String judul = (String) tableHasil.getValueAt(selected, 1);
        String hargaStr = (String) tableHasil.getValueAt(selected, 2);
        // Hapus "Rp ", lalu hapus semua titik (pemisah ribuan) dan koma (jika ada)
        double harga = Double.parseDouble(hargaStr.replace("Rp ", "").replace(".", "").replace(",", ""));
        int stokTersedia = (int) tableHasil.getValueAt(selected, 3);
        int qty;
        try {
            qty = Integer.parseInt(txtQty.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus angka > 0");
            return;
        }
        if (qty > stokTersedia) {
            JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Tersedia: " + stokTersedia);
            return;
        }
        // Cek apakah buku sudah ada di keranjang?
        for (KeranjangItem item : keranjang) {
            if (item.isbn.equals(isbn)) {
                // Update jumlah
                item.qty += qty;
                if (item.qty > stokTersedia) {
                    item.qty -= qty;
                    JOptionPane.showMessageDialog(this, "Melebihi stok!");
                    return;
                }
                item.subtotal = item.harga * item.qty;
                refreshTableKeranjang();
                hitungTotal();
                return;
            }
        }
        // Tambah baru
        double subtotal = harga * qty;
        keranjang.add(new KeranjangItem(isbn, judul, harga, qty, subtotal));
        refreshTableKeranjang();
        hitungTotal();
        txtQty.setText("");
    }

    private void refreshTableKeranjang() {
        modelKeranjang.setRowCount(0);
        for (KeranjangItem item : keranjang) {
            modelKeranjang.addRow(new Object[]{
                item.isbn, item.judul,
                "Rp " + String.format("%,d", (long)item.harga),
                item.qty,
                "Rp " + String.format("%,d", (long)item.subtotal)
            });
        }
    }

    private void editJumlahKeranjang() {
        int row = tableKeranjang.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih item di keranjang!");
            return;
        }
        KeranjangItem item = keranjang.get(row);
        String input = JOptionPane.showInputDialog(this, "Masukkan jumlah baru:", item.qty);
        if (input == null) return;
        int newQty;
        try {
            newQty = Integer.parseInt(input);
            if (newQty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah tidak valid!");
            return;
        }
        // Validasi stok dari database
        int stokSekarang = TransaksiService.getStokBuku(item.isbn);
        if (newQty > stokSekarang + item.qty) {
            JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Stok saat ini: " + stokSekarang);
            return;
        }
        item.qty = newQty;
        item.subtotal = item.harga * item.qty;
        refreshTableKeranjang();
        hitungTotal();
    }

    private void hapusItemKeranjang() {
        int row = tableKeranjang.getSelectedRow();
        if (row == -1) return;
        keranjang.remove(row);
        refreshTableKeranjang();
        hitungTotal();
    }

    private void hitungTotal() {
        double total = 0;
        for (KeranjangItem item : keranjang) {
            total += item.subtotal;
        }
        lblTotal.setText("Total: Rp " + String.format("%,d", (long)total));
    }

    private void hitungKembalian(JLabel lblKembalian) {
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang kosong!");
            return;
        }
        double total;
        try {
            String totalStr = lblTotal.getText().replace("Total: Rp ", "").replace(".", "").replace(",", "");
            total = Double.parseDouble(totalStr);
        } catch (Exception e) {
            total = 0;
        }
        double bayar;
        try {
            bayar = Double.parseDouble(txtBayar.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nominal bayar harus angka!");
            return;
        }
        if (bayar < total) {
            lblKembalian.setText("Rp " + String.format("%,d", (long)(bayar - total)));
            JOptionPane.showMessageDialog(this, "Uang kurang!");
        } else {
            lblKembalian.setText("Rp " + String.format("%,d", (long)(bayar - total)));
        }
    }

    private void prosesBayar(JLabel lblKembalian) {
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang kosong!");
            return;
        }
        double total;
        try {
            String totalStr = lblTotal.getText().replace("Total: Rp ", "").replace(".", "").replace(",", "");
            total = Double.parseDouble(totalStr);
        } catch (Exception e) {
            total = 0;
        }
        double bayar;
        try {
            bayar = Double.parseDouble(txtBayar.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nominal bayar harus angka!");
            return;
        }
        if (bayar < total) {
            JOptionPane.showMessageDialog(this, "Uang kurang, tidak bisa melanjutkan!");
            return;
        }
        String metode = (String) cbMetodeBayar.getSelectedItem();
        // Siapkan items array
        String[][] items = new String[keranjang.size()][3];
        for (int i = 0; i < keranjang.size(); i++) {
            KeranjangItem item = keranjang.get(i);
            items[i][0] = item.isbn;
            items[i][1] = String.valueOf(item.qty);
            items[i][2] = String.valueOf(item.subtotal);
        }
        boolean sukses = TransaksiService.simpanTransaksi(idKasir, total, bayar, metode, items);
        if (sukses) {
            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!\nKembalian: " + lblKembalian.getText());
            // cetak struk sederhana ke console
            cetakStruk(total, bayar, metode);
            // reset keranjang dan form
            keranjang.clear();
            refreshTableKeranjang();
            hitungTotal();
            txtBayar.setText("");
            lblKembalian.setText("Rp 0");
        } else {
            JOptionPane.showMessageDialog(this, "Transaksi gagal, cek log error!");
        }
    }

    private void cetakStruk(double total, double bayar, String metode) {
        System.out.println("========== STRUK PEMBAYARAN ==========");
        System.out.println("CHAQRIZEMY BOOKSTORE");
        System.out.println("Kasir: " + usernameKasir);
        System.out.println("Tanggal: " + new java.util.Date());
        System.out.println("--------------------------------------");
        System.out.printf("%-15s %-5s %-12s\n", "Buku", "Qty", "Subtotal");
        for (KeranjangItem item : keranjang) {
            System.out.printf("%-15s %-5d Rp %,-12.0f\n", item.judul.length()>15?item.judul.substring(0,12)+"...":item.judul, item.qty, item.subtotal);
        }
        System.out.println("--------------------------------------");
        System.out.printf("Total: Rp %,-.0f\n", total);
        System.out.printf("Bayar: Rp %,-.0f\n", bayar);
        System.out.printf("Kembali: Rp %,-.0f\n", bayar - total);
        System.out.println("Metode: " + metode);
        System.out.println("Terima kasih telah berbelanja!");
        System.out.println("======================================");
    }

    // Inner class untuk item keranjang
    private static class KeranjangItem {
        String isbn, judul;
        double harga;
        int qty;
        double subtotal;
        KeranjangItem(String isbn, String judul, double harga, int qty, double subtotal) {
            this.isbn = isbn; this.judul = judul; this.harga = harga; this.qty = qty; this.subtotal = subtotal;
        }
    }
}