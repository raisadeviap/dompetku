package com.kelenam.dompetku;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaksi")
public class Transaksi {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String tanggal;
    private String kategori; // "Pemasukan" atau "Pengeluaran"
    private String deskripsi;
    private int nominal;

    // Default constructor dibutuhkan Room
    public Transaksi() {
    }

    // Constructor manual kamu, ditandai @Ignore agar Room tidak bingung
    @Ignore
    public Transaksi(String kategori, int nominal, String deskripsi, String tanggal) {
        this.kategori = kategori;
        this.nominal = nominal;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
    }

    // Getters dan Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public int getNominal() { return nominal; }
    public void setNominal(int nominal) { this.nominal = nominal; }
}
