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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistikActivity extends AppCompatActivity {

    PieChart pieChart;
    RadioGroup radioGroup;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistik);

        pieChart = findViewById(R.id.pieChart);
        radioGroup = findViewById(R.id.radioGroup);
        db = AppDatabase.getInstance(this);

        showAllData();

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
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_statistik);
    }

    private void showAllData() {
        new Thread(() -> {
            List<Transaksi> list = db.transaksiDao().getAllTransaksi();

            int pemasukan = 0;
            int pengeluaran = 0;

            for (Transaksi t : list) {
                if (t.getJenisBarang().equalsIgnoreCase("Pemasukan")) {
                    pemasukan += t.getNominal();
                } else if (t.getJenisBarang().equalsIgnoreCase("Pengeluaran")) {
                    pengeluaran += t.getNominal();
                }
            }

            ArrayList<PieEntry> entries = new ArrayList<>();
            if (pemasukan > 0) entries.add(new PieEntry(pemasukan, "Pemasukan"));
            if (pengeluaran > 0) entries.add(new PieEntry(pengeluaran, "Pengeluaran"));

            runOnUiThread(() -> setupPieChart(entries));
        }).start();
    }

    private void showPengeluaran() {
        new Thread(() -> {
            List<Transaksi> list = db.transaksiDao().getAllTransaksi();
            Map<String, Integer> jenisMap = new HashMap<>();

            for (Transaksi t : list) {
                if (t.getJenisBarang().equalsIgnoreCase("Pengeluaran")) {
                    String jenis = t.getJenisBarang(); // Gunakan jenisBarang sebagai label
                    int value = jenisMap.getOrDefault(jenis, 0);
                    jenisMap.put(jenis, value + t.getNominal());
                }
            }

            ArrayList<PieEntry> entries = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : jenisMap.entrySet()) {
                entries.add(new PieEntry(entry.getValue(), entry.getKey()));
            }

            runOnUiThread(() -> setupPieChart(entries));
        }).start();
    }

    private void showPemasukan() {
        new Thread(() -> {
            List<Transaksi> list = db.transaksiDao().getAllTransaksi();
            Map<String, Integer> jenisMap = new HashMap<>();

            for (Transaksi t : list) {
                if (t.getJenisBarang().equalsIgnoreCase("Pemasukan")) {
                    String jenis = t.getJenisBarang(); // Gunakan jenisBarang sebagai label
                    int value = jenisMap.getOrDefault(jenis, 0);
                    jenisMap.put(jenis, value + t.getNominal());
                }
            }

            ArrayList<PieEntry> entries = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : jenisMap.entrySet()) {
                entries.add(new PieEntry(entry.getValue(), entry.getKey()));
            }

            runOnUiThread(() -> setupPieChart(entries));
        }).start();
    }

    private void setupPieChart(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");

        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            colors.add(Color.parseColor(i % 2 == 0 ? "#1E1E6C" : "#D4EAF9"));
        }

        dataSet.setColors(colors);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColors(new ArrayList<Integer>() {{
            for (int i = 0; i < entries.size(); i++) add(Color.WHITE);
        }});

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
