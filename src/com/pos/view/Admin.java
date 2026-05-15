package com.pos.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import com.pos.dao.KasirDAO;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Admin extends JFrame {

    JPanel sidebar, content;
    JTable table, tableTransaksi, tableLaporan, tableKasir;
    DefaultTableModel modelLaporan;
    BarChartPanel chartPanel;

    Color primaryColor = new Color(41, 128, 185);
    Color sidebarColor = new Color(44, 62, 80);
    Color bgColor = new Color(236, 240, 241);
    Color accentColor = new Color(46, 204, 113);
    Color warningColor = new Color(230, 126, 34);

    private Connection connectDB() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/tokobuku", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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

    // ========== FITUR TAMBAH BUKU ==========
    private void showTambahBukuDialog() {
        JDialog dialog = new JDialog(this, "Tambah Buku Baru", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField tfIsbn = new JTextField();
        JTextField tfJudul = new JTextField();
        JTextField tfHarga = new JTextField();
        JTextField tfKategori = new JTextField();
        JTextField tfStok = new JTextField();

        formPanel.add(new JLabel("ISBN:"));
        formPanel.add(tfIsbn);
        formPanel.add(new JLabel("Judul:"));
        formPanel.add(tfJudul);
        formPanel.add(new JLabel("Harga:"));
        formPanel.add(tfHarga);
        formPanel.add(new JLabel("Kategori:"));
        formPanel.add(tfKategori);
        formPanel.add(new JLabel("Stok Awal:"));
        formPanel.add(tfStok);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        btnSimpan.addActionListener(e -> {
            String isbn = tfIsbn.getText().trim();
            String judul = tfJudul.getText().trim();
            String hargaStr = tfHarga.getText().trim();
            String kategori = tfKategori.getText().trim();
            String stokStr = tfStok.getText().trim();

            if (isbn.isEmpty() || judul.isEmpty() || hargaStr.isEmpty() || kategori.isEmpty() || stokStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Semua field harus diisi!");
                return;
            }

            double harga;
            int stok;
            try {
                harga = Double.parseDouble(hargaStr);
                stok = Integer.parseInt(stokStr);
                if (harga <= 0 || stok < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Harga dan Stok harus angka positif!");
                return;
            }

            String cekSql = "SELECT isbn FROM buku WHERE isbn = ?";
            try (Connection conn = connectDB();
                 PreparedStatement ps = conn.prepareStatement(cekSql)) {
                ps.setString(1, isbn);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(dialog, "ISBN sudah terdaftar! Gunakan ISBN lain.");
                    return;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            String sql = "INSERT INTO buku (isbn, judul, harga, kategori, stok) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = connectDB();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, isbn);
                ps.setString(2, judul);
                ps.setDouble(3, harga);
                ps.setString(4, kategori);
                ps.setInt(5, stok);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Buku berhasil ditambahkan!");
                dialog.dispose();
                table.setModel(getBukuData());
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Gagal menambahkan buku: " + ex.getMessage());
            }
        });

        btnBatal.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    // ========== FITUR HAPUS BUKU ==========
    private void hapusBuku(String isbn) {
        String cekSql = "SELECT COUNT(*) FROM item_belanja WHERE isbn = ?";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(cekSql)) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Buku sudah pernah terjual. Jika dihapus, data transaksi terkait akan bermasalah.\nTetap hapus?",
                        "Peringatan", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "DELETE FROM buku WHERE isbn = ?";
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Buku berhasil dihapus!");
                table.setModel(getBukuData());
            } else {
                JOptionPane.showMessageDialog(this, "Buku tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus buku: " + e.getMessage());
        }
    }

    public Admin() {
        setTitle("CHAQRIZEMY - Admin Panel");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel logout
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTop.setBackground(bgColor);
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            new Signup();
            dispose();
        });
        panelTop.add(btnLogout);
        add(panelTop, BorderLayout.NORTH);

        // Sidebar
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
        JButton transBtn = createSidebarBtn("Riwayat Transaksi");
        JButton laporanBtn = createSidebarBtn("Laporan Penjualan");
        JButton kasirBtn = createSidebarBtn("Manajemen Kasir");

        sidebar.add(dashBtn);
        sidebar.add(bukuBtn);
        sidebar.add(transBtn);
        sidebar.add(laporanBtn);
        sidebar.add(kasirBtn);

        // Content dengan CardLayout
        content = new JPanel(new CardLayout());
        content.add(createDashboard(), "dash");
        content.add(createBukuPanel(), "buku");
        content.add(createTransaksiPanel(), "trans");
        content.add(createLaporanPenjualanPanel(), "laporan");
        content.add(createKasirPanel(), "kasir");

        dashBtn.addActionListener(e -> switchPanel("dash"));
        bukuBtn.addActionListener(e -> {
            table.setModel(getBukuData());
            switchPanel("buku");
        });
        transBtn.addActionListener(e -> {
            tableTransaksi.setModel(getTransaksiData());
            switchPanel("trans");
        });
        laporanBtn.addActionListener(e -> switchPanel("laporan"));
        kasirBtn.addActionListener(e -> {
            tableKasir.setModel(KasirDAO.getAllKasir());
            switchPanel("kasir");
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

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgColor);
        JLabel titleLabel = new JLabel("<html><span style='font-size:18px; font-weight:bold;'>Manajemen Inventaris</span></html>");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        JButton btnTambah = new JButton("+ Tambah Buku Baru");
        btnTambah.setBackground(accentColor);
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setFocusPainted(false);
        btnTambah.addActionListener(e -> showTambahBukuDialog());
        headerPanel.add(btnTambah, BorderLayout.EAST);
        p.add(headerPanel, BorderLayout.NORTH);

        table = new JTable(getBukuData());
        styleTable(table);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controls.setBackground(bgColor);
        JTextField input = new JTextField();
        input.setPreferredSize(new Dimension(100, 35));
        JButton btnUpdate = new JButton("Update Stok");
        btnUpdate.setPreferredSize(new Dimension(120, 35));
        btnUpdate.setBackground(primaryColor);
        btnUpdate.setForeground(Color.WHITE);

        JButton btnHapus = new JButton("Hapus Buku");
        btnHapus.setPreferredSize(new Dimension(120, 35));
        btnHapus.setBackground(warningColor);
        btnHapus.setForeground(Color.WHITE);

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(Admin.this, "Pilih buku terlebih dahulu!");
                return;
            }
            String isbn = table.getValueAt(row, 0).toString();
            try {
                int jml = Integer.parseInt(input.getText());
                tambahStok(isbn, jml);
                table.setModel(getBukuData());
                input.setText("");
                JOptionPane.showMessageDialog(Admin.this, "Stok berhasil diperbarui!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Admin.this, "Masukkan angka yang valid!");
            }
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(Admin.this, "Pilih buku yang akan dihapus!");
                return;
            }
            String isbn = table.getValueAt(row, 0).toString();
            String judul = table.getValueAt(row, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(Admin.this,
                    "Yakin ingin menghapus buku \"" + judul + "\"?\nData transaksi terkait juga akan terpengaruh.",
                    "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                hapusBuku(isbn);
            }
        });

        controls.add(new JLabel("Tambah/Kurangi Unit:"));
        controls.add(input);
        controls.add(btnUpdate);
        controls.add(btnHapus);
        p.add(controls, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createTransaksiPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgColor);

        JLabel title = new JLabel("Riwayat Transaksi (Detail)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(title, BorderLayout.WEST);

        JButton refreshBtn = new JButton("Refresh Data");
        refreshBtn.addActionListener(e -> tableTransaksi.setModel(getTransaksiData()));
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        p.add(headerPanel, BorderLayout.NORTH);

        tableTransaksi = new JTable(getTransaksiData());
        styleTable(tableTransaksi);
        JScrollPane sp = new JScrollPane(tableTransaksi);
        p.add(sp, BorderLayout.CENTER);

        return p;
    }

    private JPanel createLaporanPenjualanPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bgColor);
        JLabel title = new JLabel("Laporan Penjualan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(title, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(bgColor);
        filterPanel.add(new JLabel("Filter:"));
        JComboBox<String> cbFilter = new JComboBox<>(new String[]{"Hari Ini", "Minggu Ini", "Bulan Ini", "Semua"});
        JButton btnFilter = new JButton("Tampilkan");
        JLabel lblTotal = new JLabel("Total Pendapatan: Rp 0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblJumlahTransaksi = new JLabel("Jumlah Transaksi: 0");
        lblJumlahTransaksi.setFont(new Font("Segoe UI", Font.BOLD, 16));

        filterPanel.add(cbFilter);
        filterPanel.add(btnFilter);
        filterPanel.add(lblTotal);
        filterPanel.add(lblJumlahTransaksi);
        header.add(filterPanel, BorderLayout.SOUTH);
        p.add(header, BorderLayout.NORTH);

        chartPanel = new BarChartPanel("Pendapatan per Hari", new LinkedHashMap<>());
        chartPanel.setPreferredSize(new Dimension(0, 380));
        p.add(chartPanel, BorderLayout.CENTER);

        String[] kolom = {"Tanggal", "Jumlah Transaksi", "Total Pendapatan"};
        modelLaporan = new DefaultTableModel(kolom, 0);
        tableLaporan = new JTable(modelLaporan);
        styleTable(tableLaporan);
        JScrollPane scroll = new JScrollPane(tableLaporan);
        scroll.setPreferredSize(new Dimension(0, 180));
        p.add(scroll, BorderLayout.SOUTH);

        btnFilter.addActionListener(e -> {
            String selected = (String) cbFilter.getSelectedItem();
            String filterType;
            if (selected.equals("Hari Ini")) filterType = "HARI_INI";
            else if (selected.equals("Minggu Ini")) filterType = "MINGGU_INI";
            else if (selected.equals("Bulan Ini")) filterType = "BULAN_INI";
            else filterType = "SEMUA";
            loadLaporanRingkasan(filterType, lblTotal, lblJumlahTransaksi);
        });

        loadLaporanRingkasan("MINGGU_INI", lblTotal, lblJumlahTransaksi);
        cbFilter.setSelectedItem("Minggu Ini");

        return p;
    }

    // ========== FITUR MANAJEMEN KASIR ==========
    private JPanel createKasirPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(bgColor);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JLabel title = new JLabel("Manajemen Kasir");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        p.add(title, BorderLayout.NORTH);

        // Tabel daftar kasir
        tableKasir = new JTable(KasirDAO.getAllKasir());
        styleTable(tableKasir);
        p.add(new JScrollPane(tableKasir), BorderLayout.CENTER);

        // Form tambah & hapus kasir
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        form.setBackground(bgColor);

        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(150, 35));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(150, 35));

        JButton btnTambah = new JButton("Tambah Kasir");
        btnTambah.setPreferredSize(new Dimension(130, 35));
        btnTambah.setBackground(accentColor);
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setFocusPainted(false);

        JButton btnHapus = new JButton("Hapus Kasir");
        btnHapus.setPreferredSize(new Dimension(130, 35));
        btnHapus.setBackground(new Color(192, 57, 43));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setFocusPainted(false);

        btnTambah.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan password tidak boleh kosong!");
                return;
            }

            if (KasirDAO.tambahKasir(user, pass)) {
                JOptionPane.showMessageDialog(this, "Kasir '" + user + "' berhasil ditambahkan!");
                usernameField.setText("");
                passwordField.setText("");
                tableKasir.setModel(KasirDAO.getAllKasir());
            } else {
                JOptionPane.showMessageDialog(this, "Gagal! Username sudah digunakan.");
            }
        });

        btnHapus.addActionListener(e -> {
            int row = tableKasir.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih kasir yang ingin dihapus!");
                return;
            }
            String idKasir = tableKasir.getValueAt(row, 0).toString();
            int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin hapus kasir " + idKasir + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
    boolean berhasil = KasirDAO.hapusKasir(idKasir);
    if (berhasil) {
        JOptionPane.showMessageDialog(this, "Kasir berhasil dinonaktifkan!");
        tableKasir.setModel(KasirDAO.getAllKasir());
    } else {
        JOptionPane.showMessageDialog(this, "Gagal menonaktifkan kasir!");
    }
}
        });

        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        form.add(btnTambah);
        form.add(btnHapus);

        p.add(form, BorderLayout.SOUTH);
        return p;
    }

    private void loadLaporanRingkasan(String filterType, JLabel lblTotal, JLabel lblJumlahTransaksi) {
        modelLaporan.setRowCount(0);
        String sql = "SELECT DATE(tanggal) AS tgl, COUNT(*) AS jumlah, SUM(total_harga) AS total " +
                     "FROM transaksi WHERE 1=1";
        if (filterType.equals("HARI_INI")) {
            sql += " AND DATE(tanggal) = CURDATE()";
        } else if (filterType.equals("MINGGU_INI")) {
            sql += " AND YEARWEEK(tanggal, 1) = YEARWEEK(CURDATE(), 1)";
        } else if (filterType.equals("BULAN_INI")) {
            sql += " AND MONTH(tanggal) = MONTH(CURDATE()) AND YEAR(tanggal) = YEAR(CURDATE())";
        }
        sql += " GROUP BY DATE(tanggal) ORDER BY tgl ASC";

        double totalSemua = 0;
        int jumlahSemua = 0;
        Map<String, Double> dataGrafik = new LinkedHashMap<>();
        try (Connection c = connectDB();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tgl = rs.getDate("tgl").toString();
                int jumlah = rs.getInt("jumlah");
                double total = rs.getDouble("total");
                totalSemua += total;
                jumlahSemua += jumlah;
                dataGrafik.put(tgl, total);
                modelLaporan.addRow(new Object[]{tgl, jumlah, "Rp " + String.format("%,d", (long)total)});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        lblTotal.setText("Total Pendapatan: Rp " + String.format("%,d", (long)totalSemua));
        lblJumlahTransaksi.setText("Jumlah Transaksi: " + jumlahSemua);
        chartPanel.updateData(dataGrafik);
    }

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

    // ======================= INNER CLASS BAR CHART =======================
    class BarChartPanel extends JPanel {
        private Map<String, Double> data;
        private String title;

        public BarChartPanel(String title, Map<String, Double> data) {
            this.title = title;
            this.data = data;
            setBackground(Color.WHITE);
            setBorder(new EmptyBorder(20, 20, 20, 20));
        }

        public void updateData(Map<String, Double> data) {
            this.data = data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (data == null || data.isEmpty()) {
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                g2.setColor(Color.GRAY);
                g2.drawString("Tidak ada data untuk periode ini", 50, 100);
                return;
            }

            int leftPadding = 70;
            int rightPadding = 40;
            int topPadding = 60;
            int bottomPadding = 60;

            int chartWidth = getWidth() - leftPadding - rightPadding;
            int chartHeight = getHeight() - topPadding - bottomPadding;

            int x0 = leftPadding;
            int y0 = getHeight() - bottomPadding;

            g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
            g2.setColor(new Color(40, 40, 40));
            g2.drawString(title, leftPadding, 35);

            double max = data.values().stream().max(Double::compare).orElse(1.0);
            max *= 1.4;

            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(x0, topPadding, x0, y0);
            g2.drawLine(x0, y0, x0 + chartWidth, y0);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            int gridCount = 5;
            for (int i = 0; i <= gridCount; i++) {
                int y = y0 - (i * chartHeight / gridCount);
                g2.setColor(new Color(220, 220, 220));
                g2.drawLine(x0, y, x0 + chartWidth, y);
            }

            int totalBar = data.size();
            int space = 30;
            int barWidth = (chartWidth - (space * (totalBar + 1))) / totalBar;
            if (barWidth < 10) barWidth = 10;
            int currentX = x0 + space;

            for (Map.Entry<String, Double> entry : data.entrySet()) {
                double value = entry.getValue();
                int barHeight = (int) ((value / max) * chartHeight);
                if (barHeight < 2) barHeight = 2;
                int barY = y0 - barHeight;

                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(currentX + 3, barY + 3, barWidth, barHeight, 12, 12);

                g2.setColor(primaryColor);
                g2.fillRoundRect(currentX, barY, barWidth, barHeight, 12, 12);

                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                g2.setColor(new Color(50, 50, 50));
                String valueText = formatRupiah((long) value);
                int textWidth = g2.getFontMetrics().stringWidth(valueText);
                g2.drawString(valueText,
                    currentX + (barWidth / 2) - (textWidth / 2),
                    barY - 5);

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                String date = entry.getKey();
                if (date.length() >= 10) date = date.substring(5);
                int labelWidth = g2.getFontMetrics().stringWidth(date);
                g2.drawString(date,
                    currentX + (barWidth / 2) - (labelWidth / 2),
                    y0 + 20);

                currentX += barWidth + space;
            }
        }

        private String formatRupiah(long value) {
            return "Rp " + String.format("%,d", value).replace(",", ".");
        }
    }
    // ======================= AKHIR BAR CHART =======================

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        new Admin();
    }
}