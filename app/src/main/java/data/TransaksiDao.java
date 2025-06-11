package data;

import androidx.room.*;
import java.util.List;

@Dao
public interface TransaksiDao {

    @Query("SELECT * FROM transaksi ORDER BY tanggal DESC")
    List<Transaksi> getAllTransaksi();

    @Insert
    void insertTransaksi(Transaksi transaksi);

    @Update
    void updateTransaksi(Transaksi transaksi);

    @Delete
    void deleteTransaksi(Transaksi transaksi);
}