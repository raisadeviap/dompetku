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

        // Set jenis barang dan catatan
        holder.tvJenisBarang.setText(transaksi.getJenisBarang());
        holder.tvCatatan.setText(transaksi.getCatatan());

        // Format tanggal
        try {
            long timestamp = Long.parseLong(transaksi.getTanggal());
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", new Locale("id", "ID"));
            holder.tvTanggal.setText(sdf.format(date));
        } catch (Exception e) {
            holder.tvTanggal.setText(transaksi.getTanggal());
        }

        // Format nominal dan ikon
        int nominal = transaksi.getNominal();
        if (nominal >= 0) {
            holder.tvNominal.setText("+Rp. " + nominal);
            holder.imgIcon.setImageResource(R.drawable.pemasukan);
        } else {
            holder.tvNominal.setText("-Rp. " + Math.abs(nominal));
            holder.imgIcon.setImageResource(R.drawable.pengeluaran);
        }
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvJenisBarang, tvCatatan, tvNominal, tvTanggal;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJenisBarang = itemView.findViewById(R.id.tvJenisBarang);
            tvCatatan = itemView.findViewById(R.id.tvCatatan);
            tvNominal = itemView.findViewById(R.id.tvNominal);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }
}
