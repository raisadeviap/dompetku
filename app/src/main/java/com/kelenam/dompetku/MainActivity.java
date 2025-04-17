package com.kelenam.dompetku;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kelenam.dompetku.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener ke BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_beranda);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.nav_beranda) {
                        // Jika sudah di Beranda, tidak perlu melakukan apa-apa
                        return true;
                    } else if (id == R.id.nav_tambah) {
                        startActivity(new Intent(MainActivity.this, TambahActivity.class));
                        overridePendingTransition(0, 0); // Menghilangkan animasi transisi
                        finish(); // Menutup MainActivity
                        return true;
                    } else if (id == R.id.nav_statistik) {
                        startActivity(new Intent(MainActivity.this, StatistikActivity.class));
                        overridePendingTransition(0, 0); // Menghilangkan animasi transisi
                        finish(); // Menutup MainActivity
                        return true;
                    }

                    return false;
                }
            };
}