package apps.njl.gosafe.RoomDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Trip.class}, version = 1, exportSchema = false)
public abstract class TripDB extends RoomDatabase {

    public abstract TripDao tripDao();

}
