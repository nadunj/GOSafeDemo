package apps.njl.gosafe;

import androidx.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import apps.njl.gosafe.RoomDB.TripDB;

public class MainMenu extends AppCompatActivity {

    private ImageButton btn_navigation, btn_neabyIncident, btn_history, btn_profile, btn_settings;
    private TripDB tripDB;
    private ConstraintLayout layout;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String token="";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        token = getIntent().getStringExtra("token");
        initializeConponents();
        initializeMethods();

    }

    /**Initialze component**/
    private void initializeConponents() {
        layout = findViewById(R.id.layout_home);
        btn_navigation = findViewById(R.id.btn_mnu_navigate);
        btn_neabyIncident = findViewById(R.id.btn_mnu_incidents);
        btn_profile = findViewById(R.id.btn_mnu_profile);
        btn_settings = findViewById(R.id.btn_mnu_settings);
        sharedPref = getSharedPreferences("GoSafe_settings", 0);
        editor = sharedPref.edit();

//        tripDB = Room.databaseBuilder(getApplicationContext(), TripDB.class, "TripDB").fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
//                .build();
    }

    /**Initialize methods**/
    private void initializeMethods() {
        btn_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainMenu.this,Navigation.class);
                startActivity(intent);
            }
        });

        /**start settings activity**/
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, SettingsActivity.class));
            }
        });

        /**start nearby activity**/
        btn_neabyIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainMenu.this,NearbyMap.class);
                startActivity(intent);

            }
        });

        /**Start driving history activity**/
      /*  btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(token!=null) {
                    if (tripDB.tripDao().getAllTrips().size() == 0)
                        setMessage("No Records Found!");
                    else {
                        intent = new Intent(MainMenu.this,DriverHistory.class);
                        intent.putExtra("token",token);
                        startActivity(intent);
                    }
                }else {
                setMessage("Please login to access this service");
            }
            }
        }); */

        /**start profile activity**/
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainMenu.this, ProfileActivity.class);
                startActivity(intent);
            }});
    }

    private void setMessage(String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
