package apps.njl.gosafe;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import REST_Controller.RESTClient;
import REST_Controller.RESTInterface;
import plugins.gligerglg.locusservice.LocusService;

public class NearbyMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocusService locusService;
    private MaterialDialog dialog;
    private Location myLocation;
    private ConstraintLayout layout;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String token;

    private double distance;
    private LatLng myLocationLatLng, incidentLatLng;

    private boolean isDataGet = true;
    private int radius = 0;
    private SharedPreferences sharedPreferences;
    private RESTInterface restInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nearby_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Init();

        locusService.setRealTimeLocationListener(new LocusService.RealtimeListenerService() {
            @Override
            public void OnRealLocationChanged(Location location) {
                if (location != null) {
                    myLocation = location;
                    myLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    dialog.dismiss();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())).title("My Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 10.0f));
                    locusService.stopRealTimeNetListening();
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        //Move camera to Sri Lanka
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(7.5414423, 80.6452276), 10.0f));
    }

    private void Init() {
        token = getIntent().getStringExtra("token");
        layout = findViewById(R.id.nearby_Layout);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("RT-Incidents");
        locusService = new LocusService(getApplicationContext(), false);

        restInterface = RESTClient.getInstance().create(RESTInterface.class);
        sharedPreferences = getSharedPreferences("GoSafe_settings", 0);
        radius = sharedPreferences.getInt("radius", 0);

    }

    public void getRealtimeIncidents(View view) {
        if (myLocation != null) {
            if (isDataGet) {
                mMap.clear();
                setProgressDialog("Downloading Real-Time Data");
                getRealtimeData();
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 13.0f));
        } else
            getGPSLocation();
    }

    /*public void getBlackspotLocations(View view) {
        mMap.clear();
        if (myLocation != null) {
            setProgressDialog("Downloading Blackspot Data");
            NearbyRequest request = new NearbyRequest();
            request.setLatitude(myLocation.getLatitude());
            request.setLongitude(myLocation.getLongitude());
            request.setRadius(radius);
            getBlackspotData(request);
        } else
            setPopupMessage("Please enable GPS & try again!");
    }

    public void getTrafficIncidents(View view) {
        mMap.clear();
        if (myLocation != null) {
            setProgressDialog("Downloading TrafficSign Data");
            NearbyRequest request = new NearbyRequest();
            request.setLatitude(myLocation.getLatitude());
            request.setLongitude(myLocation.getLongitude());
            request.setRadius(radius);
            getTrafficData(request);
        } else
            setPopupMessage("Please enable GPS & try again!");

    }

    public void getSpeedLocations(View view) {
        mMap.clear();
        if (myLocation != null) {
            setProgressDialog("Downloading Speed Point Data");
            NearbyRequest request = new NearbyRequest();
            request.setLatitude(myLocation.getLatitude());
            request.setLongitude(myLocation.getLongitude());
            request.setRadius(radius);
            getSpeedLimitData(request);
        } else
            setPopupMessage("Please enable GPS & try again!");
    }

    public void getCriticalLocations(View view) {
        mMap.clear();
        if (myLocation != null) {
            setProgressDialog("Downloading Critical Data");
            NearbyRequest request = new NearbyRequest();
            request.setLatitude(myLocation.getLatitude());
            request.setLongitude(myLocation.getLongitude());
            request.setRadius(radius);
            getCriticalData(request);
        } else
            setPopupMessage("Please enable GPS & try again!");
    }*/


    private void setProgressDialog(String message) {
        dialog = new MaterialDialog.Builder(NearbyMap.this)
                .content(message)
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    private void getGPSLocation() {
        if (myLocation == null) {
            if (locusService.isNetProviderEnabled()) {
                locusService.startRealtimeNetListening(1000);
                setProgressDialog("Calculating GPS Location");
            } else
                setPopupMessage("Please enable GPS connectivity");
        }
    }

    private void setPopupMessage(String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getGPSLocation();
    }

    /**
     * REST Methods
     **/
    private void getRealtimeData() {
        isDataGet = false;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RealtimeIncident incident = snapshot.getValue(RealtimeIncident.class);
                    incidentLatLng = new LatLng(incident.getLatitude(), incident.getLongitude());
                    distance = MapController.getDistance(myLocationLatLng, incidentLatLng);
                    if (distance <= radius) {
                        mMap.addCircle(new CircleOptions().strokeWidth(2).radius(50).fillColor(0x2200ff00)
                                .strokeColor(Color.TRANSPARENT).center(new LatLng(incident.getLatitude(), incident.getLongitude())));

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(incident.getLatitude(), incident.getLongitude()))
                                .title(incident.getIncident_name())
                                .icon(BitmapDescriptorFactory.fromResource(MapController.mapMarkerIcon(incident.getIncident_name()))));
                    }
                }
                myRef.removeEventListener(this);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                setPopupMessage(databaseError.getMessage());
                dialog.dismiss();
                isDataGet = true;
            }
        });

    }

    /*private void getTrafficData(NearbyRequest request) {
        isDataGet = true;
        Call<List<TrafficSign>> call = restInterface.getTrafficData(request);
        call.enqueue(new Callback<List<TrafficSign>>() {
            @Override
            public void onResponse(Call<List<TrafficSign>> call, Response<List<TrafficSign>> response) {
                if (response.body() != null) {
                    for (TrafficSign sign : response.body()) {
                        mMap.addCircle(new CircleOptions().strokeWidth(2).radius(50).fillColor(0x2200ff00)
                                .strokeColor(Color.TRANSPARENT).center(new LatLng(sign.getLatitude(), sign.getLongitude())));

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(sign.getLatitude(), sign.getLongitude()))
                                .title(sign.getSign())
                                .icon(BitmapDescriptorFactory.fromResource(MapController.mapStaticIcon("Traffic"))));
                    }
                    dialog.dismiss();
                } else {
                    setPopupMessage("No Road Signs Found");
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<TrafficSign>> call, Throwable t) {
                setPopupMessage("" + t.getMessage());
                dialog.dismiss();
            }
        });
    }

    private void getBlackspotData(NearbyRequest request) {
        isDataGet = true;
        Call<List<Blackspot>> call = restInterface.getBlackspotData(request);
        call.enqueue(new Callback<List<Blackspot>>() {
            @Override
            public void onResponse(Call<List<Blackspot>> call, Response<List<Blackspot>> response) {
                if (response.body() != null) {
                    for (Blackspot sign : response.body()) {
                        mMap.addCircle(new CircleOptions().strokeWidth(2).radius(sign.getRadius()).fillColor(0x220000ff)
                                .strokeColor(Color.TRANSPARENT).center(new LatLng(sign.getLatitude(), sign.getLongitude())));

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(sign.getLatitude(), sign.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(MapController.mapStaticIcon("Black-Spot"))));
                    }
                    dialog.dismiss();
                } else {
                    setPopupMessage("No Black-spots Found");
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Blackspot>> call, Throwable t) {
                setPopupMessage("" + t.getMessage());
                dialog.dismiss();
            }
        });

    }

    private void getSpeedLimitData(NearbyRequest request) {
        isDataGet = true;
        Call<List<SpeedLimit>> call = restInterface.getSpeedLimitData(request);
        call.enqueue(new Callback<List<SpeedLimit>>() {
            @Override
            public void onResponse(Call<List<SpeedLimit>> call, Response<List<SpeedLimit>> response) {
                if (response.body() != null) {
                    for (SpeedLimit sign : response.body()) {
                        mMap.addCircle(new CircleOptions().strokeWidth(2).radius(sign.getRadius()).fillColor(0x22ffff00)
                                .strokeColor(Color.TRANSPARENT).center(new LatLng(sign.getLatitude(), sign.getLongitude())));

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(sign.getLatitude(), sign.getLongitude()))
                                .title("Speed Limit : " + sign.getSpeedLimit() + " kmh")
                                .icon(BitmapDescriptorFactory.fromResource(MapController.mapStaticIcon("Speed"))));
                    }
                    dialog.dismiss();
                } else {
                    setPopupMessage("No Speed Points Found");
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<SpeedLimit>> call, Throwable t) {
                setPopupMessage("" + t.getMessage());
                dialog.dismiss();
            }
        });
    }

    private void getCriticalData(NearbyRequest request) {
        isDataGet = true;
        Call<List<CriticalLocation>> call = restInterface.getCriticalData(request);
        call.enqueue(new Callback<List<CriticalLocation>>() {
            @Override
            public void onResponse(Call<List<CriticalLocation>> call, Response<List<CriticalLocation>> response) {
                if (response.body() != null) {
                    for (CriticalLocation sign : response.body()) {
                        mMap.addCircle(new CircleOptions().strokeWidth(2).radius(sign.getRadius()).fillColor(0x22ff0000)
                                .strokeColor(Color.TRANSPARENT).center(new LatLng(sign.getLatitude(), sign.getLongitude())));

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(sign.getLatitude(), sign.getLongitude()))
                                .title(sign.getMessage() + "")
                                .icon(BitmapDescriptorFactory.fromResource(MapController.mapStaticIcon("Critical"))));
                    }
                    dialog.dismiss();
                } else {
                    setPopupMessage("No Critical Location Found");
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<CriticalLocation>> call, Throwable t) {
                setPopupMessage("" + t.getMessage());
                dialog.dismiss();
            }
        });
    }*/

}
