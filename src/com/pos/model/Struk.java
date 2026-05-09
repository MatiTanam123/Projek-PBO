package com.pos.model;

import java.util.ArrayList;
import java.util.List;

public class Struk {
    private String idTransaksi;
    private String tanggal;
    private String namaKasir;
    private double totalHarga;
    private double nominalBayar;
    private List<String[]> items; // Isi: {Nama Buku, Qty, Harga, Subtotal}

    public Struk(String idTransaksi, String tanggal, String namaKasir, double totalHarga, double nominalBayar) {
        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.namaKasir = namaKasir;
        this.totalHarga = totalHarga;
        this.nominalBayar = nominalBayar;
        this.items = new ArrayList<>();
    }

    public void addItem(String namaBuku, String qty, String harga, String subtotal) {
        this.items.add(new String[]{namaBuku, qty, harga, subtotal});
    }

    // Getter untuk digunakan di CetakService
    public String getIdTransaksi() { return idTransaksi; }
    public String getTanggal() { return tanggal; }
    public String getNamaKasir() { return namaKasir; }
    public double getTotalHarga() { return totalHarga; }
    public double getNominalBayar() { return nominalBayar; }
    public double getKembalian() { return nominalBayar - totalHarga; }
    public List<String[]> getItems() { return items; }
}