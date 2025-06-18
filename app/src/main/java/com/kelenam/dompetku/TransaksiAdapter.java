package com.kelenam.dompetku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder> {

    private List<Transaksi> transaksiList;

    public TransaksiAdapter(List<Transaksi> transaksiList) {
        this.transaksiList = transaksiList;
    }

    @NonNull
    @Override
    public TransaksiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaksi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiAdapter.ViewHolder holder, int position) {
        Transaksi transaksi = transaksiList.get(position);
        Context context = holder.itemView.getContext();

        // Set kategori dan deskripsi
        holder.tvKategori.setText(transaksi.getKategori());
        holder.tvDeskripsi.setText(transaksi.getDeskripsi());

        // Format tanggal
        try {
            long timestamp = Long.parseLong(transaksi.getTanggal());
            String formattedDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    .format(new Date(timestamp));
            holder.tvTanggal.setText(formattedDate);
        } catch (Exception e) {
            holder.tvTanggal.setText(transaksi.getTanggal()); // fallback
        }

        // Format nominal & tampilkan ikon sesuai kategori
        if (transaksi.getKategori().equalsIgnoreCase("Pengeluaran")) {
            holder.imgIcon.setImageResource(R.drawable.pengeluaran);
            holder.tvNominal.setText("-Rp" + transaksi.getNominal());
            holder.tvNominal.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else {
            holder.imgIcon.setImageResource(R.drawable.pemasukan);
            holder.tvNominal.setText("+Rp" + transaksi.getNominal());
            holder.tvNominal.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        }
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvKategori, tvDeskripsi, tvNominal, tvTanggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvKategori = itemView.findViewById(R.id.tvKategori);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvNominal = itemView.findViewById(R.id.tvNominal);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
        }
    }
}
