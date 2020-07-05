package apps.njl.gosafe.RoomDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Trip.class}, version = 1, exportSchema = false)
public abstract class TripDB extends RoomDatabase {

    public abstract TripDao tripDao();

}
