package data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Transaksi.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TransaksiDao transaksiDao();
}
