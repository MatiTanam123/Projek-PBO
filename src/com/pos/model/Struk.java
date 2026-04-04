package com.pos.model;

public class Struk {
    public void printToConsole(Transaksi t) {
        System.out.println("=== CAJEFA BOOKSTORE ===");
        System.out.println("Total: " + t.getTotalHarga());
        System.out.println("========================");
    }
}
