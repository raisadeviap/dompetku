package com.kelenam.dompetku;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

public class TambahActivity extends AppCompatActivity {

    private Spinner spinnerBarang, spinnerJenis;
    private TextInputEditText textSaldo;
    private TextInputEditText etCatatan;
    private Button btnTambah;
    private BottomNavigationView bottomNavigationView;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        // Inisialisasi database
        db = AppDatabase.getInstance(this);

        // Inisialisasi view
        spinnerBarang = findViewById(R.id.spinnerBarang);
        spinnerJenis = findViewById(R.id.spinnerJenis);
        textSaldo = findViewById(R.id.textSaldo);
        etCatatan = findViewById(R.id.etCatatan);
        btnTambah = findViewById(R.id.btnTambah);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Bottom Navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_tambah);

        // Spinner data
        String[] barangKategori = {"Makanan", "Pakaian", "Uang Jajan", "Gaji Freelance", "Lainnya"};
        String[] jenisKategori = {"Pemasukan", "Pengeluaran"};

        ArrayAdapter<String> barangAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, barangKategori);
        barangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBarang.setAdapter(barangAdapter);

        ArrayAdapter<String> jenisAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenisKategori);
        jenisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenis.setAdapter(jenisAdapter);

        // Tombol tambah
        btnTambah.setOnClickListener(v -> {
            String saldoStr = textSaldo.getText().toString();
            String barang = spinnerBarang.getSelectedItem().toString();
            String jenis = spinnerJenis.getSelectedItem().toString();
            String catatan = etCatatan.getText().toString();

            if (saldoStr.isEmpty() || catatan.isEmpty()) {
                Toast.makeText(this, "Isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            int saldo;
            try {
                saldo = Integer.parseInt(saldoStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Nominal tidak valid!", Toast.LENGTH_SHORT).show();
                return;
            }

            String tanggal = String.valueOf(System.currentTimeMillis());

            // Sesuai urutan konstruktor Transaksi: kategori, nominal, deskripsi, tanggal
            Transaksi transaksi = new Transaksi(jenis, saldo, catatan + " - " + barang, tanggal);

            // Simpan ke database secara async
            new Thread(() -> {
                try {
                    db.transaksiDao().insert(transaksi);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Transaksi berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } catch (Exception e) {
                    e.printStackTrace(); // log ke Logcat
                    runOnUiThread(() ->
                            Toast.makeText(this, "Gagal menambahkan transaksi", Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.nav_beranda) {
                        startActivity(new Intent(TambahActivity.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.nav_tambah) {
                        return true;
                    } else if (id == R.id.nav_statistik) {
                        startActivity(new Intent(TambahActivity.this, StatistikActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }

                    return false;
                }
            };
}
