package apps.njl.gosafe;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class HistorySpeedMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<SpeedMarker> speedMarkersList;
    private List<LatLng> wayPointList = new ArrayList<>();
    private static final int lowerSpeed = 40;
    private static final int midSpeed = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_speed_map);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Init();
    }

    /**
     * Initialize all attributes
     */
    private void Init() {
        TypeToken<List<SpeedMarker>> token = new TypeToken<List<SpeedMarker>>() {
        };
        String speedPointString = getIntent().getStringExtra("speedMarkerList");
        speedMarkersList = new Gson().fromJson(speedPointString, token.getType());

        for (SpeedMarker point : speedMarkersList)
            wayPointList.add(new LatLng(point.getLatitude(), point.getLongitude()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        drawSpeedPoints();
    }

    /**
     * Plot speed points imported from DB to map fragment
     */
    private void drawSpeedPoints() {
        MapController.drawPolyline(getApplicationContext(), wayPointList, R.color.colorPrimaryDark, mMap);
        MapController.setCameraBounds(wayPointList.get(0), wayPointList.get(wayPointList.size() - 1), mMap);

        for (SpeedMarker marker : speedMarkersList) {
            if (marker.getSpeed() < lowerSpeed)
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(marker.getLatitude(), marker.getLongitude()))
                        .title("Lower Speed")
                        .snippet(MapController.generateSpeedString(marker.getSpeed()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.low_speed_marker)));
            else if (marker.getSpeed() < midSpeed)
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(marker.getLatitude(), marker.getLongitude()))
                        .title("Mid Speed")
                        .snippet(MapController.generateSpeedString(marker.getSpeed()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mid_speed_marker)));
            else if (marker.getSpeed() >= midSpeed)
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(marker.getLatitude(), marker.getLongitude()))
                        .title("Higher Speed")
                        .snippet(MapController.generateSpeedString(marker.getSpeed()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.high_speed_marker)));
        }
    }
}
