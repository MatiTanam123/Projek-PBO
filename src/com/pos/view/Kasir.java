package com.pos.view;

import com.pos.service.TransaksiService;
import com.pos.config.Koneksi;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

// Import iText classes (hanya yang dibutuhkan, JANGAN import Font)
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Kasir extends JFrame {

    private JTextField txtCari, txtQty, txtBayar;
    private JComboBox<String> cbMetodeBayar;
    private JTable tableKeranjang;
    private DefaultTableModel modelKeranjang;
    private JLabel lblTotal;
    private String idKasir;
    private String usernameKasir;
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

        JPanel main = new JPanel(new GridLayout(1, 2, 10, 10));
        main.setBorder(new EmptyBorder(10, 10, 10, 10));
        main.setBackground(new Color(236, 240, 241));

        // ===== PANEL KIRI =====
        JPanel panelKiri = new JPanel(new BorderLayout(5, 5));
        panelKiri.setBackground(Color.WHITE);

        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCari.add(new JLabel("Cari Buku (ISBN / Judul):"));
        txtCari = new JTextField(20);
        panelCari.add(txtCari);
        JButton btnCari = new JButton("Cari");
        panelCari.add(btnCari);

        String[] kolomHasil = {"ISBN", "Judul", "Harga", "Stok"};
        DefaultTableModel modelHasil = new DefaultTableModel(kolomHasil, 0);
        JTable tableHasil = new JTable(modelHasil);
        tableHasil.setRowHeight(30);
        JScrollPane scrollHasil = new JScrollPane(tableHasil);
        scrollHasil.setPreferredSize(new Dimension(0, 200));

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

        String[] kolomKeranjang = {"ISBN", "Judul", "Harga", "Qty", "Subtotal"};
        modelKeranjang = new DefaultTableModel(kolomKeranjang, 0);
        tableKeranjang = new JTable(modelKeranjang);
        tableKeranjang.setRowHeight(30);
        JScrollPane scrollKeranjang = new JScrollPane(tableKeranjang);
        
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

        // ===== PANEL KANAN =====
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

    private void loadDataAwal() {}

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
        for (KeranjangItem item : keranjang) {
            if (item.isbn.equals(isbn)) {
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
            cetakStrukPDF(total, bayar, metode);
            keranjang.clear();
            refreshTableKeranjang();
            hitungTotal();
            txtBayar.setText("");
            lblKembalian.setText("Rp 0");
        } else {
            JOptionPane.showMessageDialog(this, "Transaksi gagal, cek log error!");
        }
    }

    private void cetakStrukPDF(double total, double bayar, String metode) {
        try {
            File folder = new File("struk");
            if (!folder.exists()) folder.mkdir();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String namaFile = "struk/Struk_" + timestamp + ".pdf";
            Document document = new Document(PageSize.A6);
            PdfWriter.getInstance(document, new FileOutputStream(namaFile));
            document.open();

            // Gunakan nama lengkap untuk iText Font (karena tidak diimport)
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);
            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font smallFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 8, com.itextpdf.text.Font.NORMAL);

            document.add(new Paragraph("CHAQRIZEMY BOOKSTORE", titleFont));
            document.add(new Paragraph("Jl. Contoh No. 123, Pontianak", smallFont));
            document.add(new Paragraph("Telp: 0812-3456-7890", smallFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Kasir: " + usernameKasir, normalFont));
            document.add(new Paragraph("Tanggal: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), normalFont));
            document.add(new Paragraph("----------------------------------------", normalFont));
            document.add(new Paragraph(String.format("%-12s %4s %8s", "Nama Buku", "Qty", "Subtotal"), boldFont));
            document.add(new Paragraph("----------------------------------------", normalFont));

            for (KeranjangItem item : keranjang) {
                String judul = item.judul;
                if (judul.length() > 12) judul = judul.substring(0, 10) + "..";
                document.add(new Paragraph(String.format("%-12s %4d Rp %8s",
                    judul, item.qty, formatRupiah((long)item.subtotal)), normalFont));
            }

            document.add(new Paragraph("----------------------------------------", normalFont));
            document.add(new Paragraph(String.format("%-18s Rp %10s", "Total:", formatRupiah((long)total)), boldFont));
            document.add(new Paragraph(String.format("%-18s Rp %10s", "Bayar:", formatRupiah((long)bayar)), normalFont));
            document.add(new Paragraph(String.format("%-18s Rp %10s", "Kembali:", formatRupiah((long)(bayar - total))), normalFont));
            document.add(new Paragraph("Metode: " + metode, normalFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Terima kasih telah berbelanja!", boldFont));
            document.add(new Paragraph("Simpan struk ini sebagai bukti.", smallFont));

            document.close();
            JOptionPane.showMessageDialog(this, "Struk PDF telah disimpan di folder 'struk'");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mencetak PDF: " + e.getMessage());
        }
    }

    private String formatRupiah(long value) {
        return String.format("%,d", value).replace(",", ".");
    }

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