package com.kelenam.dompetku;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistikActivity extends AppCompatActivity {

    private PieChart pieChart;
    private RadioGroup radioGroup;
    private RecyclerView recyclerView;
    private TransaksiAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistik);

        pieChart = findViewById(R.id.pieChart);
        radioGroup = findViewById(R.id.radioGroup);
        recyclerView = findViewById(R.id.recyclerViewStatistik);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = AppDatabase.getInstance(getApplicationContext());

        showAllData();
        loadCatatan();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSemua) {
                showAllData();
            } else if (checkedId == R.id.rbPengeluaran) {
                showPengeluaran();
            } else if (checkedId == R.id.rbPemasukan) {
                showPemasukan();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_statistik);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    private void showAllData() {
        new Thread(() -> {
            List<Transaksi> transaksiList = db.transaksiDao().getAllTransaksi();
            int totalPemasukan = 0;
            int totalPengeluaran = 0;

            for (Transaksi t : transaksiList) {
                if (t.getKategori().equalsIgnoreCase("Pemasukan")) {
                    totalPemasukan += t.getNominal();
                } else if (t.getKategori().equalsIgnoreCase("Pengeluaran")) {
                    totalPengeluaran += t.getNominal();
                }
            }

            ArrayList<PieEntry> entries = new ArrayList<>();
            if (totalPemasukan > 0) entries.add(new PieEntry(totalPemasukan, "Pemasukan"));
            if (totalPengeluaran > 0) entries.add(new PieEntry(totalPengeluaran, "Pengeluaran"));

            runOnUiThread(() -> setupPieChart(entries, "all"));
        }).start();
    }

    private void showPengeluaran() {
        new Thread(() -> {
            List<Transaksi> transaksiList = db.transaksiDao().getAllTransaksi();

            // Mengelompokkan pengeluaran berdasarkan deskripsi atau sub-kategori
            Map<String, Integer> kategoriMap = new HashMap<>();

            for (Transaksi t : transaksiList) {
                if (t.getKategori().equalsIgnoreCase("Pengeluaran")) {
                    // Gunakan deskripsi sebagai sub-kategori
                    String subKategori = t.getDeskripsi(); // Asumsikan ada field deskripsi

                    // Jika deskripsi kosong, gunakan kategori umum
                    if (subKategori == null || subKategori.trim().isEmpty()) {
                        subKategori = "Pengeluaran Lainnya";
                    }

                    int nominal = t.getNominal();

                    if (kategoriMap.containsKey(subKategori)) {
                        kategoriMap.put(subKategori, kategoriMap.get(subKategori) + nominal);
                    } else {
                        kategoriMap.put(subKategori, nominal);
                    }
                }
            }

            ArrayList<PieEntry> entries = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : kategoriMap.entrySet()) {
                if (entry.getValue() > 0) {
                    entries.add(new PieEntry(entry.getValue(), entry.getKey()));
                }
            }

            // Jika tidak ada sub-kategori, tampilkan total pengeluaran
            if (entries.isEmpty()) {
                int totalPengeluaran = 0;
                for (Transaksi t : transaksiList) {
                    if (t.getKategori().equalsIgnoreCase("Pengeluaran")) {
                        totalPengeluaran += t.getNominal();
                    }
                }
                if (totalPengeluaran > 0) {
                    entries.add(new PieEntry(totalPengeluaran, "Total Pengeluaran"));
                }
            }

            runOnUiThread(() -> setupPieChart(entries, "pengeluaran"));
        }).start();
    }

    private void showPemasukan() {
        new Thread(() -> {
            List<Transaksi> transaksiList = db.transaksiDao().getAllTransaksi();

            // Mengelompokkan pemasukan berdasarkan deskripsi atau sub-kategori
            Map<String, Integer> kategoriMap = new HashMap<>();

            for (Transaksi t : transaksiList) {
                if (t.getKategori().equalsIgnoreCase("Pemasukan")) {
                    // Gunakan deskripsi sebagai sub-kategori
                    String subKategori = t.getDeskripsi(); // Asumsikan ada field deskripsi

                    // Jika deskripsi kosong, gunakan kategori umum
                    if (subKategori == null || subKategori.trim().isEmpty()) {
                        subKategori = "Pemasukan Lainnya";
                    }

                    int nominal = t.getNominal();

                    if (kategoriMap.containsKey(subKategori)) {
                        kategoriMap.put(subKategori, kategoriMap.get(subKategori) + nominal);
                    } else {
                        kategoriMap.put(subKategori, nominal);
                    }
                }
            }

            ArrayList<PieEntry> entries = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : kategoriMap.entrySet()) {
                if (entry.getValue() > 0) {
                    entries.add(new PieEntry(entry.getValue(), entry.getKey()));
                }
            }

            // Jika tidak ada sub-kategori, tampilkan total pemasukan
            if (entries.isEmpty()) {
                int totalPemasukan = 0;
                for (Transaksi t : transaksiList) {
                    if (t.getKategori().equalsIgnoreCase("Pemasukan")) {
                        totalPemasukan += t.getNominal();
                    }
                }
                if (totalPemasukan > 0) {
                    entries.add(new PieEntry(totalPemasukan, "Total Pemasukan"));
                }
            }

            runOnUiThread(() -> setupPieChart(entries, "pemasukan"));
        }).start();
    }

    private void setupPieChart(ArrayList<PieEntry> entries, String type) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Integer> valueTextColors = new ArrayList<>();

        if (type.equals("all")) {
            // Untuk tampilan semua data (Pemasukan vs Pengeluaran)
            for (PieEntry e : entries) {
                switch (e.getLabel()) {
                    case "Pemasukan":
                        colors.add(Color.parseColor("#1E1E6C")); // Biru tua
                        valueTextColors.add(Color.WHITE);
                        break;
                    case "Pengeluaran":
                    default:
                        colors.add(Color.parseColor("#D4EAF9")); // Biru muda
                        valueTextColors.add(Color.parseColor("#1E1E6C"));
                        break;
                }
            }
        } else {
            // Untuk pengeluaran dan pemasukan, gunakan variasi warna biru
            String[] blueShades = {
                    "#1E1E6C", "#D4EAF9", "#3F51B5", "#5C6BC0", "#7986CB",
                    "#9FA8DA", "#C5CAE9", "#283593", "#303F9F", "#1A237E"
            };

            for (int i = 0; i < entries.size(); i++) {
                colors.add(Color.parseColor(blueShades[i % blueShades.length]));
                // Tentukan warna text berdasarkan warna background
                if (i % 2 == 0) {
                    valueTextColors.add(Color.WHITE); // Text putih untuk warna gelap
                } else {
                    valueTextColors.add(Color.parseColor("#1E1E6C")); // Text biru tua untuk warna terang
                }
            }
        }

        dataSet.setColors(colors);
        dataSet.setValueTextColors(valueTextColors);
        dataSet.setValueTextSize(14f);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false); // Sesuai dengan kode asli
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setCenterText("");
        pieChart.invalidate();
    }

    private void loadCatatan() {
        new Thread(() -> {
            List<Transaksi> transaksiList = db.transaksiDao().getAllTransaksi();
            runOnUiThread(() -> {
                adapter = new TransaksiAdapter(transaksiList);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.nav_beranda) {
                        startActivity(new Intent(StatistikActivity.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.nav_tambah) {
                        startActivity(new Intent(StatistikActivity.this, TambahActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.nav_statistik) {
                        return true;
                    }
                    return false;
                }
            };
}