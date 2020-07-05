package apps.njl.gosafe.RoomDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TripDao {
    @Query("SELECT * FROM Trip_Table")
    List<Trip> getAllTrips();

    @Insert
    void insertTrip(Trip... trips);

    @Delete
    void deleteTrip(Trip trip);

    @Query("DELETE FROM Trip_Table")
    void deleteAll();

}
