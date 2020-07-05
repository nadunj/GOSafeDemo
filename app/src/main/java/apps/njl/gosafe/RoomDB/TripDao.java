package apps.njl.gosafe.RoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

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
