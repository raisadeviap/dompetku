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

    PieChart pieChart;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistik);

        // Inisialisasi view
        pieChart = findViewById(R.id.pieChart);
        radioGroup = findViewById(R.id.radioGroup);

        // Default chart
        showAllData();

        // Listener untuk toggle menu
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

    // Fungsi untuk masing-masing jenis grafik
    private void showAllData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(1000, "Pemasukan"));
        entries.add(new PieEntry(200, "Pengeluaran"));
        setupPieChart(entries);
    }

    private void showPengeluaran() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(100, "Makanan"));
        entries.add(new PieEntry(400, "Pakaian"));
        setupPieChart(entries);
    }

    private void showPemasukan() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(1000, "Pemasukan"));
        setupPieChart(entries);
    }

    private void setupPieChart(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Integer> valueTextColors = new ArrayList<>();

        for (PieEntry e : entries) {
            switch (e.getLabel()) {
                case "Pemasukan":
                case "Pakaian":
                    colors.add(Color.parseColor("#1E1E6C"));
                    valueTextColors.add(Color.WHITE);
                    break;
                case "Makanan":
                default:
                    colors.add(Color.parseColor("#D4EAF9"));
                    valueTextColors.add(Color.parseColor("#1E1E6C"));
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


    // Navigasi Bottom Nav
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
