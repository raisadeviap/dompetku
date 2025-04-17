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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        // Inisialisasi BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_tambah); // Set item aktif

        // Inisialisasi view lainnya
        spinnerBarang = findViewById(R.id.spinnerBarang);
        spinnerJenis = findViewById(R.id.spinnerJenis);
        textSaldo = findViewById(R.id.textSaldo);
        etCatatan = findViewById(R.id.etCatatan);
        btnTambah = findViewById(R.id.btnTambah);

        // Data Spinner
        String[] barangKategori = {"Makanan", "Pakaian", "Uang Jajan", "Gaji Freelance", "Lainnya"};
        String[] jenisKategori = {"Pemasukan", "Pengeluaran"};

        // Adapter Spinner
        ArrayAdapter<String> barangAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, barangKategori);
        barangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBarang.setAdapter(barangAdapter);

        ArrayAdapter<String> jenisAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenisKategori);
        jenisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenis.setAdapter(jenisAdapter);

        // Event Tombol Tambah
        btnTambah.setOnClickListener(v -> {
            String saldo = textSaldo.getText().toString();
            String barang = spinnerBarang.getSelectedItem().toString();
            String jenis = spinnerJenis.getSelectedItem().toString();
            String catatan = etCatatan.getText().toString();

            if (saldo.isEmpty() || catatan.isEmpty()) {
                Toast.makeText(this, "Isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this,
                    "Saldo: " + saldo +
                            "\nKategori: " + barang +
                            "\nJenis: " + jenis +
                            "\nCatatan: " + catatan,
                    Toast.LENGTH_LONG).show();
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
                        // Sudah di TambahActivity, tidak perlu melakukan apa-apa
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