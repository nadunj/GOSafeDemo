package apps.njl.gosafe;

import androidx.room.Room;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import apps.njl.gosafe.RoomDB.Trip;
import apps.njl.gosafe.RoomDB.TripDB;

public class DriverHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DriverHistoryAdapter adapter;
    private TripDB tripDB;
    private List<Trip> trips;
    private TextView txt_totalDistance, txt_totalTime, txt_totalScore;
    private double distance=0, time = 0;
    private int score = 0;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_history);

        /**Initialize all attributes**/
        Init();
    }

    /**Initializing**/
    private void Init() {
        txt_totalDistance = findViewById(R.id.txt_history_distance);
        txt_totalTime = findViewById(R.id.txt_history_duration);
        txt_totalScore = findViewById(R.id.txt_history_points);


        tripDB = Room.databaseBuilder(getApplicationContext(),TripDB.class,"TripDB").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        trips = tripDB.tripDao().getAllTrips();
        for(Trip trip : trips){
            distance+=trip.getTotalDistance();
            time += trip.getTotalDuration();
            score += trip.getTotal_score();
        }

        txt_totalDistance.setText("" + MapController.generateSimpleDistanceString(distance));
        txt_totalTime.setText("" + MapController.generateSimpleTimeString(time));
        txt_totalScore.setText(score + " Points");

        recyclerView = findViewById(R.id.history_view);
        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DriverHistoryAdapter(trips);
        recyclerView.setAdapter(adapter);

    }
}
