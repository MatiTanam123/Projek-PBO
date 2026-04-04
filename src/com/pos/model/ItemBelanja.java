package com.pos.model;

public class ItemBelanja {
   private Buku buku;
    private int qty;
    private double subtotal;

    public ItemBelanja(Buku buku, int qty) {
        this.buku = buku;
        this.qty = qty;
        this.subtotal = buku.getHarga() * qty;
    }

    public double getSubtotal() { return subtotal; }
    public Buku getBuku() { return buku; }
    public int getQty() { return qty; }
}
