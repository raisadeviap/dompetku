package com.kelenam.dompetku;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StatistikActivity extends AppCompatActivity {

    private PieChart pieChart;
    private RadioGroup radioGroup;
    private AppDatabase db; // Tambahkan referensi database Room

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistik);

        // Inisialisasi Room Database
        db = AppDatabase.getInstance(this);

        // Inisialisasi view
        pieChart = findViewById(R.id.pieChart);
        radioGroup = findViewById(R.id.radioGroup);

        // Tampilkan data default
        showAllData();

        // Toggle pilihan radio
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSemua) {
                showAllData();
            } else if (checkedId == R.id.rbPengeluaran) {
                showPengeluaran();
            } else if (checkedId == R.id.rbPemasukan) {
                showPemasukan();
            }
        });

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_statistik);
    }

    // Menampilkan grafik semua kategori
    private void showAllData() {
        new Thread(() -> {
            int totalPemasukan = db.transaksiDao().getTotalByKategori("Pemasukan");
            int totalPengeluaran = db.transaksiDao().getTotalByKategori("Pengeluaran");

            ArrayList<PieEntry> entries = new ArrayList<>();
            if (totalPemasukan > 0) entries.add(new PieEntry(totalPemasukan, "Pemasukan"));
            if (totalPengeluaran > 0) entries.add(new PieEntry(totalPengeluaran, "Pengeluaran"));

            runOnUiThread(() -> setupPieChart(entries));
        }).start();
    }


    // Menampilkan hanya pemasukan
    private void showPemasukan() {
        new Thread(() -> {
            int totalPemasukan = db.transaksiDao().getTotalByKategori("Pemasukan");

            ArrayList<PieEntry> entries = new ArrayList<>();
            if (totalPemasukan > 0) entries.add(new PieEntry(totalPemasukan, "Pemasukan"));

            runOnUiThread(() -> setupPieChart(entries));
        }).start();
    }

    // Menampilkan hanya pengeluaran
    private void showPengeluaran() {
        new Thread(() -> {
            int totalPengeluaran = db.transaksiDao().getTotalByKategori("Pengeluaran");

            ArrayList<PieEntry> entries = new ArrayList<>();
            if (totalPengeluaran > 0) entries.add(new PieEntry(totalPengeluaran, "Pengeluaran"));

            runOnUiThread(() -> setupPieChart(entries));
        }).start();
    }

    // Setup PieChart
    private void setupPieChart(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Integer> valueTextColors = new ArrayList<>();

        for (PieEntry e : entries) {
            switch (e.getLabel()) {
                case "Pemasukan":
                    colors.add(Color.parseColor("#1E1E6C"));
                    valueTextColors.add(Color.WHITE);
                    break;
                case "Pengeluaran":
                    colors.add(Color.parseColor("#D4EAF9"));
                    valueTextColors.add(Color.parseColor("#1E1E6C"));
                    break;
                default:
                    colors.add(Color.GRAY);
                    valueTextColors.add(Color.BLACK);
                    break;
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
        pieChart.getLegend().setEnabled(false);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setCenterText("");
        pieChart.invalidate();
    }

    // Navigasi Bottom Navigation
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
