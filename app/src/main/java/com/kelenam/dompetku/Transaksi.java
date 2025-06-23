package com.kelenam.dompetku;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaksi")
public class Transaksi {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String tanggal;
    private String jenisBarang; // sebelumnya kategori
    private String catatan;     // sebelumnya deskripsi
    private int nominal;

    // Default constructor dibutuhkan Room
    public Transaksi() {
    }

    // Constructor manual
    @Ignore
    public Transaksi(String jenisBarang, int nominal, String catatan, String tanggal) {
        this.jenisBarang = jenisBarang;
        this.nominal = nominal;
        this.catatan = catatan;
        this.tanggal = tanggal;
    }

    // Getters dan Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJenisBarang() {
        return jenisBarang;
    }

    public void setJenisBarang(String jenisBarang) {
        this.jenisBarang = jenisBarang;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }
}
