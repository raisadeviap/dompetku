package data;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaksi")
public class Transaksi {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public double jumlah;
    public String kategori;
    public String jenis;      // "Pemasukan" / "Pengeluaran"
    public String tanggal;    // yyyy-MM-dd
    public String catatan;
}
