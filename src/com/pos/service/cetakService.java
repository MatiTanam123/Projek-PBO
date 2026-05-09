package com.pos.service;

import com.pos.model.Struk;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public class cetakService {
    public static void generatePDF(Struk struk) {
        Document document = new Document();
        try {
            // Nama file berdasarkan ID Transaksi
            String fileName = "struk_" + struk.getIdTransaksi() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();
            
            // Header
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("CHAQRIZEMY BOOKSTORE", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            document.add(new Paragraph("ID Transaksi: " + struk.getIdTransaksi()));
            document.add(new Paragraph("Tanggal: " + struk.getTanggal()));
            document.add(new Paragraph("Kasir: " + struk.getNamaKasir()));
            document.add(new Paragraph(" ")); // Spasi

            // Tabel Item
            PdfPTable table = new PdfPTable(4); // 4 Kolom
            table.addCell("Buku");
            table.addCell("Qty");
            table.addCell("Harga");
            table.addCell("Subtotal");

            for (String[] item : struk.getItems()) {
                table.addCell(item[0]); // Nama
                table.addCell(item[1]); // Qty
                table.addCell(item[2]); // Harga
                table.addCell(item[3]); // Subtotal
            }
            document.add(table);

            // Total
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total Harga: Rp " + struk.getTotalHarga()));
            document.add(new Paragraph("Bayar: Rp " + struk.getNominalBayar()));
            document.add(new Paragraph("Kembalian: Rp " + struk.getKembalian()));
            
            document.add(new Paragraph("\n--- Terima Kasih Telah Berbelanja ---"));

            document.close();
            System.out.println("PDF Berhasil Dibuat: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ini buat testing doang y
    
    public static void main(String[] args) {
    // 1. Siapkan data dummy (pura-puranya ada transaksi)
    Struk strukDummy = new Struk("TRK-20260510", "10 Mei 2026", "Chantiqia", 150000.0, 200000.0);
    
    // 2. Tambahkan item belanja dummy
    strukDummy.addItem("Pemrograman Java Dasar", "1", "100000", "100000");
    strukDummy.addItem("Algoritma & Struktur Data", "1", "50000", "50000");

    // 3. Panggil fungsi cetak
    System.out.println("Sedang mencetak PDF...");
    generatePDF(strukDummy);
}
}
