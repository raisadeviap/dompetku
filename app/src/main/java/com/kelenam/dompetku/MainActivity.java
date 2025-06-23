package com.kelenam.dompetku;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kelenam.dompetku.AppDatabase;
import com.kelenam.dompetku.Transaksi;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppDatabase db;
    private TransaksiAdapter adapter;
    private BottomNavigationView bottomNavigationView;

    // TextView saldo, pemasukan, pengeluaran
    private TextView textSaldo, saldoPemasukan, saldoPengeluaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi view
        recyclerView = findViewById(R.id.recyclerViewTransaksi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // FIXED

        textSaldo = findViewById(R.id.textSaldo);
        saldoPemasukan = findViewById(R.id.saldoPemasukan);
        saldoPengeluaran = findViewById(R.id.saldoPengeluaran);

        db = AppDatabase.getInstance(getApplicationContext()); // FIXED
        loadTransaksi();

        // Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_beranda);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTransaksi();
    }

    private void loadTransaksi() {
        new Thread(() -> {
            List<Transaksi> transaksiList = db.transaksiDao().getAllTransaksi();

            final int[] totalPemasukan = {0};
            final int[] totalPengeluaran = {0};

            for (Transaksi t : transaksiList) {
                if (t.getKategori().equalsIgnoreCase("Pemasukan")) {
                    totalPemasukan[0] += t.getNominal();
                } else if (t.getKategori().equalsIgnoreCase("Pengeluaran")) {
                    totalPengeluaran[0] += t.getNominal();
                }
            }

            final int totalSaldo = totalPemasukan[0] - totalPengeluaran[0];

            runOnUiThread(() -> {
                adapter = new TransaksiAdapter(transaksiList);
                recyclerView.setAdapter(adapter);

                textSaldo.setText("Rp " + formatRupiah(totalSaldo));
                saldoPemasukan.setText("Rp " + formatRupiah(totalPemasukan[0]));
                saldoPengeluaran.setText("Rp " + formatRupiah(totalPengeluaran[0]));
            });
        }).start();
    }

    private String formatRupiah(int amount) {
        return String.format("%,d", amount).replace(',', '.'); // Contoh: 1.000.000
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.nav_beranda) {
                        return true;
                    } else if (id == R.id.nav_tambah) {
                        startActivity(new Intent(MainActivity.this, TambahActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.nav_statistik) {
                        startActivity(new Intent(MainActivity.this, StatistikActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }

                    return false;
                }
            };
}
