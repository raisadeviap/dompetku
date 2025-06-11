package com.dicoding.associate.coba;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.List;

import data.AppDatabase;
import data.Transaksi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi Room database
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "dompetku.db"
        ).allowMainThreadQueries().build();

        // Data dummy
        Transaksi transaksi = new Transaksi();
        transaksi.catatan = "Beli Kopi";
        transaksi.jumlah = 15000;
        transaksi.kategori = "Minuman";
        transaksi.jenis = "Pengeluaran";
        transaksi.tanggal = "2025-06-11";

        // Insert ke database
        db.transaksiDao().insertTransaksi(transaksi);

        // Ambil semua transaksi dari database
        List<Transaksi> semuaTransaksi = db.transaksiDao().getAllTransaksi();

        // Tampilkan di Logcat
        for (Transaksi t : semuaTransaksi) {
            Log.d("CekDatabase", "Catatan: " + t.catatan + ", Jumlah: " + t.jumlah + ", Kategori: " + t.kategori +
                    ", Jenis: " + t.jenis + ", Tanggal: " + t.tanggal);
        }
    }
}
