package com.pos.model;

public class Buku {
    private String isbn;
    private String judul;
    private double harga;
    private String kategori;
    private int stok;

    public Buku(String isbn, String judul, double harga, String kategori, int stok) {
        this.isbn = isbn;
        this.judul = judul;
        this.harga = harga;
        this.kategori = kategori;
        this.stok = stok;
    }

    // ngecek stok buku
    public boolean isStokTersedia(int qty) {
        return this.stok >= qty;
    }

    // ngurangin stok buku kalo transaksi berhasil
    public void kurangiStok(int qty) {
        if (isStokTersedia(qty)) {
            this.stok -= qty;
        }
    }

    // nambah stok pas admin restok
    public void tambahStok(int qty) {
        this.stok += qty;
    }

    // Getter & Setter dasar
    public String getIsbn() { return isbn; }
    public String getJudul() { return judul; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
}
