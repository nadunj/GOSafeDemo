package apps.njl.gosafe;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import REST_Controller.RESTClient;
import REST_Controller.RESTInterface;
import REST_Controller.RouteRequest;
import REST_Controller.StaticIncidentData;
import apps.njl.gosafe.services.MyLocationService;
import plugins.gligerglg.locusservice.LocusService;

public class Navigation extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {

    private final int LOCATION_REQUEST_CODE = 1000;
    private GoogleMap mMap;
    private LatLng myPosition = null, destination = null;
    private MaterialDialog dialog;
    private double myLocLat, myLocLon, desLat, desLon;
    private boolean isReroute = false;
    private CoordinatorLayout layout;
    private FloatingActionButton btn_gps;
    private PlaceAutocompleteFragment autocompleteFragment;
    private AutocompleteFilter autocompleteFilter;
    private List<Polyline> polylines;
    private String destination_name;
    private Route selected_path;
    private MataraLatLngGenerator mataraLatLngGenerator;
    private boolean failedRoute = false;
    private HashMap<Polyline, Route> routeHashMap = new HashMap<>();
    private static final int[] COLORS = new int[]{R.color.colorPrimary, R.color.alternativeRouteColor};

    private int realtime_incidents = 0;
    private int blackspots = 0;
    private int speedpoints = 0;
    private int critical = 0;
    private int traffic = 0;

    private RESTInterface restInterface;
    private StaticIncidentData statics_data;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String token;

    private boolean isBlackspotEnabled, isCriticalEnabled, isTrafficEnabled, isSpeedLimitEnabled;
    private SharedPreferences sharedPref;

    private MyLocationService myLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_navigation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Init();
        token = getIntent().getStringExtra("token");

        btn_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(Navigation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(Navigation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Navigation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_REQUEST_CODE);
                } else {
                    initLocationService();
                }
            }
        });


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                    destination = place.getLatLng();
                    destination_name = place.getName().toString();
                    mMap.clear();
                    if (myPosition != null)
                        mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location"));
                    mMap.addMarker(new MarkerOptions().position(destination).title(destination_name).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 14.0f));

                    if (myPosition != null) {
                        setProgressDialog("Fetching Route Data");
                        route();
                    }

            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            initLocationService();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        //route();
        Log.e("Error", e.getMessage());
        dialog.dismiss();
        setMessage(e.getMessage());

        failedRoute = true;
        mataraLatLngGenerator = new MataraLatLngGenerator();
        myPosition = mataraLatLngGenerator.getStartPoint();
        destination = mataraLatLngGenerator.getDestination();
        MapController.drawPolyline(getApplicationContext(),mataraLatLngGenerator.getGeneratedList(),R.color.colorPrimaryDark,mMap);

        setProgressDialog("Fetching Incident Data");
        showStaticIncidentData();
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> routes, int shortestRouteIndex) {
        polylines.clear();
        routeHashMap.clear();
        /**Get shortest path as selected path*/
        selected_path = routes.get(shortestRouteIndex);
        showRealtimeData(selected_path);
        for (int i = 0; i < routes.size(); i++) {
            if (i == shortestRouteIndex)
                continue;
            else
                drawRoute(routes.get(i), 1);
        }

        drawRoute(routes.get(shortestRouteIndex), 0);
        MapController.setCameraBounds(myPosition, destination, mMap);
        btn_gps.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_navigation));
        try {
            dialog.dismiss();
        } catch (Exception e) {
        }

    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Move camera to Sri Lanka
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(7.5414423, 80.6452276), 7.0f));
        mMap.getUiSettings().setMapToolbarEnabled(false);

        if (isReroute) {
            route();
        }

        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                selected_path = routeHashMap.get(polyline);
                showRealtimeData(selected_path);
                mMap.clear();
                for (Polyline poly : polylines)
                    MapController.drawPolyline(getApplicationContext(), poly.getPoints(), R.color.alternativeRouteColor, mMap);
                MapController.drawPolyline(getApplicationContext(), polyline.getPoints(), R.color.colorPrimary, mMap);

                mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin)));
                mMap.addMarker(new MarkerOptions().position(destination).title(destination_name).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin)));
            }
        });


        /////////////////
        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng));
                mataraList.add(latLng);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mataraList.remove(mataraList.indexOf(marker.getPosition()));
                marker.remove();
                return false;
            }
        });*/

        ///////////////////
    }

    private void setProgressDialog(String message) {
        dialog = new MaterialDialog.Builder(Navigation.this)
                .content(message)
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    /**
     * Draw shortest & alternative routes
     */
    private void drawRoute(Route route, int colorIndex) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(COLORS[colorIndex]));
        polyOptions.width(10);
        polyOptions.addAll(route.getPoints());
        Polyline polyline = mMap.addPolyline(polyOptions);
        polylines.add(polyline);
        routeHashMap.put(polyline, route);
        polyline.setClickable(true);
    }

    private void setMessage(String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void initLocationService(){
        setProgressDialog("Calculating GPS Location");
        myLocationService = new MyLocationService(Navigation.this, new MyLocationService.LocationUpdate() {
            @Override
            public void onLocationFetched(Location location) {
                mMap.clear();
                dialog.dismiss();
                myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 14.0f));
                if (destination != null) {
                    setProgressDialog("Fetching Route Data");
                    route();
                }
            }
        });
    }

    private void Init() {
        sharedPref = getSharedPreferences("GoSafe_settings", 0);
        isReroute = getIntent().getBooleanExtra("isReroute", false);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("RT-Incidents");


        if (isReroute) {
            myLocLat = getIntent().getDoubleExtra("myLocLat", 0);
            myLocLon = getIntent().getDoubleExtra("myLocLon", 0);
            desLat = getIntent().getDoubleExtra("desLat", 0);
            desLon = getIntent().getDoubleExtra("desLon", 0);
            myPosition = new LatLng(myLocLat, myLocLon);
            destination = new LatLng(desLat, desLon);
        }

        layout = findViewById(R.id.main_coordinatorLayout);
        btn_gps = findViewById(R.id.main_fab_gps);

        autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("LK")
                .build();
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Destination");
        autocompleteFragment.setFilter(autocompleteFilter);
        polylines = new ArrayList<>();

        restInterface = RESTClient.getInstance().create(RESTInterface.class);
        initSettingsData();

    }

    /**Notify the user about the number of incident data related to selected route*/
    private void showPathInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation.this, R.style.Theme_AppCompat_Dialog_Alert);
        View view_dialog = getLayoutInflater().inflate(R.layout.choose_best_route_fragment, null);

        final TextView txt_totalDistance = view_dialog.findViewById(R.id.txt_total_distance);
        final TextView txt_totalDuration = view_dialog.findViewById(R.id.txt_total_time);
        final TextView txt_realtime_Incident = view_dialog.findViewById(R.id.txt_realtime_incidents);
        final TextView txt_blackspots = view_dialog.findViewById(R.id.txt_blackspots);
        final TextView txtcriticalLocation = view_dialog.findViewById(R.id.txt_criticalLocations);
        final TextView txt_speedPoint = view_dialog.findViewById(R.id.txt_speedPoints);
        final TextView txt_traffic = view_dialog.findViewById(R.id.txt_trafficSigns);
        TextView btn_navigate = view_dialog.findViewById(R.id.btn_route_navigate);

        if(failedRoute){
            txt_totalDistance.setText("3.4km");
            txt_totalDuration.setText("8 min");
        }else {
            txt_totalDistance.setText(selected_path.getDistanceText());
            txt_totalDuration.setText(selected_path.getDurationText());
        }
        txt_realtime_Incident.setText("" + realtime_incidents);
        txt_blackspots.setText("" + blackspots);
        txtcriticalLocation.setText("" + critical);
        txt_speedPoint.setText("" + speedpoints);
        txt_traffic.setText("" + traffic);


        final Intent intent = new Intent(getApplicationContext(), MapsNavigate.class);
        RouteInfo routeInfo;

        if(failedRoute)
            routeInfo = new RouteInfo(mataraLatLngGenerator.getStartPoint(), mataraLatLngGenerator.getDestination(),mataraLatLngGenerator.getGeneratedList(), "Nilwala Gate Bus Stop",
                    94000, 1080);
        else
            routeInfo = new RouteInfo(myPosition, destination, selected_path.getPoints(), destination_name,
                    selected_path.getDistanceValue(), selected_path.getDurationValue());

        //Static Filter
        if (!isBlackspotEnabled)
            statics_data.getBlackSpots().clear();
        if (!isTrafficEnabled)
            statics_data.getRoadSigns().clear();
        if (!isSpeedLimitEnabled)
            statics_data.getSpeedLimits().clear();
        if (!isCriticalEnabled)
            statics_data.getCriticalPoints().clear();

        //Static Data Class
        String static_data = new Gson().toJson(statics_data);
        String route_data = new Gson().toJson(routeInfo);
        intent.putExtra("token",token);
        intent.putExtra("route", route_data);
        intent.putExtra("staticdata", static_data);

        btn_navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                finish();
            }
        });

        try {
            builder.setView(view_dialog);
            builder.create().show();
        } catch (Exception ex) {
        }

    }

    private void route() {
        Log.d("Route", "Started");
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin)));
        mMap.addMarker(new MarkerOptions().position(destination).title(destination_name).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin)));

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(myPosition, destination)
                .build();
        routing.execute();
    }

    /**Request static location data from server using REST APIs**/
    private void showStaticIncidentData() {
        ArrayList<RouteRequest> requestList = new ArrayList<>();

        if(failedRoute){
            for (LatLng point : mataraLatLngGenerator.getGeneratedList())
                requestList.add(new RouteRequest(point.latitude, point.longitude));
        }else {
            for (LatLng point : selected_path.getPoints())
                requestList.add(new RouteRequest(point.latitude, point.longitude));
        }

        /*Call<StaticIncidentData> call = restInterface.getStaticDataSet(requestList);
        call.enqueue(new Callback<StaticIncidentData>() {
            @Override
            public void onResponse(Call<StaticIncidentData> call, Response<StaticIncidentData> response) {
                if (response.body() != null) {
                    try {
                        statics_data = response.body();
                        blackspots = statics_data.getBlackSpots().size();
                        critical = statics_data.getCriticalPoints().size();
                        speedpoints = statics_data.getSpeedLimits().size();
                        traffic = statics_data.getRoadSigns().size();
                    }catch (Exception ex){}
                }
                showPathInfoDialog();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<StaticIncidentData> call, Throwable t) {
                setMessage("Error : " + t.getMessage());
                dialog.dismiss();
            }
        });*/

    }

    /**Request real-time location data from Google Firebase**/
    private void showRealtimeData(final Route route) {
        realtime_incidents = 0;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myRef.removeEventListener(this);
                if (dataSnapshot.getChildren().iterator().hasNext()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        RealtimeIncident incident = snapshot.getValue(RealtimeIncident.class);
                        LatLng incidentLatLng = new LatLng(incident.getLatitude(), incident.getLongitude());

                        for (LatLng point : route.getPoints()) {
                            if (incidentLatLng.equals(point)) {
                                realtime_incidents++;
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initSettingsData() {
        isBlackspotEnabled = sharedPref.getBoolean("blackspot", true);
        isCriticalEnabled = sharedPref.getBoolean("critical", true);
        isSpeedLimitEnabled = sharedPref.getBoolean("speed", true);
        isTrafficEnabled = sharedPref.getBoolean("traffic", true);
    }

}
