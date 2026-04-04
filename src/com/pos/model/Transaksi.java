package com.pos.model;
import java.util.ArrayList;
import java.util.Date;

public class Transaksi {
    private String idTransaksi;
    private Date tanggal;
    private ArrayList<ItemBelanja> keranjang;
    private double totalHarga;
    private String metodeBayar;

    public Transaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
        this.tanggal = new Date();
        this.keranjang = new ArrayList<>();
    }

    public void tambahItem(Buku buku, int qty) {
        if (buku.isStokTersedia(qty)) {
            keranjang.add(new ItemBelanja(buku, qty));
            hitungTotal();
        }
    }

    public void hitungTotal() {
        totalHarga = 0;
        for (ItemBelanja item : keranjang) {
            totalHarga += item.getSubtotal();
        }
    }

    public double hitungKembalian(double nominalBayar) {
        return nominalBayar - totalHarga;
    }

    // Getter
    public double getTotalHarga() { return totalHarga; }
}
