package com.kelenam.dompetku;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransaksiDao {

    @Insert
    void insert(Transaksi transaksi);

    @Delete
    void delete(Transaksi transaksi);

    @Query("SELECT * FROM transaksi ORDER BY id DESC")
    List<Transaksi> getAllTransaksi();

    @Query("SELECT SUM(nominal) FROM transaksi WHERE kategori = :kategori")
    int getTotalByKategori(String kategori);
}
