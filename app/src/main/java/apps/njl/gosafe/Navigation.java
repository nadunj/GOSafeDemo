package apps.njl.gosafe;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import apps.njl.gosafe.adapters.LocationAdapter;
import apps.njl.gosafe.bean.GoogleLocation;
import apps.njl.gosafe.services.MyLocationService;

public class Navigation extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {

    private final int LOCATION_REQUEST_CODE = 1000;
    private GoogleMap mMap;
    private LatLng myPosition = null, destination = null;
    private MaterialDialog dialog;
    private double myLocLat, myLocLon, desLat, desLon;
    private boolean isReroute = false;
    private CoordinatorLayout layout;
    private FloatingActionButton btn_gps;
    private List<Polyline> polylines;
    private String destination_name;
    private Route selected_path;
    private MataraLatLngGenerator mataraLatLngGenerator;
    private boolean failedRoute = false;
    private HashMap<Polyline, Route> routeHashMap = new HashMap<>();
    private static final int[] COLORS = new int[]{R.color.colorPrimary, R.color.alternativeRouteColor};

    private EditText txtQuery;
    private Button btnSearch;
    private boolean isRouteSuccess = false;

    private int realtime_incidents = 0;
    private int blackspots = 0;
    private int speedpoints = 0;
    private int critical = 0;
    private int traffic = 0;

    private RESTInterface restInterface;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String token;

    private boolean isBlackspotEnabled, isCriticalEnabled, isTrafficEnabled, isSpeedLimitEnabled;
    private SharedPreferences sharedPref;
    private MyLocationService myLocationService;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList = new ArrayList<>();
    private LocationAdapter locationAdapter;
    private Marker myMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_navigation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));
        placesClient = Places.createClient(this);

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
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initLocationService();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        //route();
        Log.e("Error", e.getMessage());
        dialog.dismiss();
        setMessage(e.getMessage());

        isRouteSuccess = true;

        failedRoute = true;
        mataraLatLngGenerator = new MataraLatLngGenerator();
        myPosition = mataraLatLngGenerator.getStartPoint();
//        destination = mataraLatLngGenerator.getDestination();
        MapController.drawPolyline(getApplicationContext(), mataraLatLngGenerator.getGeneratedList(), R.color.colorPrimaryDark, mMap);

        setProgressDialog("Fetching Incident Data");
//        showStaticIncidentData();
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> routes, int shortestRouteIndex) {
        polylines.clear();
        routeHashMap.clear();
        isRouteSuccess = true;
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

//        showPathInfoDialog();

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

    private void initLocationService() {
        setProgressDialog("Calculating GPS Location");
        myLocationService = new MyLocationService(Navigation.this, new MyLocationService.LocationUpdate() {
            @Override
            public void onLocationFetched(Location location) {
//                mMap.clear();
                if (dialog != null)
                    dialog.dismiss();
                myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                if (myMarker != null) myMarker.remove();
                myMarker = mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 14.0f));
                if (destination != null) {
                    if (!isRouteSuccess) {
                        setProgressDialog("Fetching Route Data");
                        route();
                    }
                }
            }
        });
    }

    private void Init() {
        sharedPref = getSharedPreferences("GoSafe_settings", 0);
        isReroute = getIntent().getBooleanExtra("isReroute", false);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("RT-Incidents");

        txtQuery = findViewById(R.id.txtQuery);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtQuery.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Please enter place name to search", Toast.LENGTH_SHORT).show();
                else {
                    AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
                    FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                            .setCountry("lk")
                            .setTypeFilter(TypeFilter.ESTABLISHMENT)
                            .setSessionToken(token)
                            .setQuery(txtQuery.getText().toString().trim())
                            .build();

                    placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                        predictionList.clear();
                        predictionList.addAll(response.getAutocompletePredictions());

                        if (predictionList.size() == 0) {
                            Toast.makeText(getApplicationContext(), "No Results found!", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Navigation.this);
                            LayoutInflater inflater = LayoutInflater.from(Navigation.this);
                            View content = inflater.inflate(R.layout.location_dialog, null);
                            builder.setView(content);
                            RecyclerView list = content.findViewById(R.id.recView);
                            list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            list.setAdapter(locationAdapter);
                            Dialog dialog1 = builder.create();
                            dialog1.show();


                            locationAdapter = new LocationAdapter(predictionList, getApplicationContext(), new LocationAdapter.LocationCallBack() {
                                @Override
                                public void onPlaceTapped(AutocompletePrediction autocompletePrediction) {
                                    dialog1.dismiss();
                                    setProgressDialog("Request Location Data");
                                    RequestQueue queue = Volley.newRequestQueue(Navigation.this);
                                    String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + autocompletePrediction.getPlaceId() + "&key=" + getResources().getString(R.string.api_key);
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    GoogleLocation googleLocation = new Gson().fromJson(response, GoogleLocation.class);
                                                    destination = new LatLng(googleLocation.getResult().getGeometry().getLocation().getLat(), googleLocation.getResult().getGeometry().getLocation().getLng());
                                                    mMap.addMarker(new MarkerOptions().position(destination).title(autocompletePrediction.getPrimaryText(null).toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_pin)));
                                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 12));
                                                    dialog.dismiss();
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });
                                    queue.add(stringRequest);
                                }
                            });

                            locationAdapter.notifyDataSetChanged();
                        }

                    }).addOnFailureListener((exception) -> {
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Toast.makeText(getApplicationContext(), "Place not found: " + apiException.getStatusCode(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


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

        polylines = new ArrayList<>();

        restInterface = RESTClient.getInstance().create(RESTInterface.class);


    }

    /**
     * Notify the user about the number of incident data related to selected route
     */
    private void showPathInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation.this, R.style.Theme_AppCompat_Dialog_Alert);
        View view_dialog = getLayoutInflater().inflate(R.layout.choose_best_route_fragment, null);

        final TextView txt_totalDistance = view_dialog.findViewById(R.id.txt_total_distance);
        final TextView txt_totalDuration = view_dialog.findViewById(R.id.txt_total_time);
        final TextView txt_realtime_Incident = view_dialog.findViewById(R.id.txt_realtime_incidents);
        TextView btn_navigate = view_dialog.findViewById(R.id.btn_route_navigate);

        if (failedRoute) {
            txt_totalDistance.setText("3.4km");
            txt_totalDuration.setText("8 min");
        } else {
            txt_totalDistance.setText(selected_path.getDistanceText());
            txt_totalDuration.setText(selected_path.getDurationText());
        }
        txt_realtime_Incident.setText("" + realtime_incidents);



        final Intent intent = new Intent(getApplicationContext(), MapsNavigate.class);
        RouteInfo routeInfo;

        if (failedRoute)
            routeInfo = new RouteInfo(mataraLatLngGenerator.getStartPoint(), mataraLatLngGenerator.getDestination(), mataraLatLngGenerator.getGeneratedList(), "Nilwala Gate Bus Stop",
                    94000, 1080);
        else
            routeInfo = new RouteInfo(myPosition, destination, selected_path.getPoints(), destination_name,
                    selected_path.getDistanceValue(), selected_path.getDurationValue());


        //Static Data Class;
        BaseApplication.routeInfo = routeInfo;
        intent.putExtra("token", token);

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
                .key(getString(R.string.api_key))
                .alternativeRoutes(true)
                .waypoints(myPosition, destination)
                .build();
        routing.execute();
    }

    /**
     * Request static location data from server using REST APIs
     **/
    private void showStaticIncidentData() {
        ArrayList<RouteRequest> requestList = new ArrayList<>();

        if (failedRoute) {
            for (LatLng point : mataraLatLngGenerator.getGeneratedList())
                requestList.add(new RouteRequest(point.latitude, point.longitude));
        } else {
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

    /**
     * Request real-time location data from Google Firebase
     **/
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

                        showPathInfoDialog();

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
