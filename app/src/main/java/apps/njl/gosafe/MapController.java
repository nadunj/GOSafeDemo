package apps.njl.gosafe;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Static Methods to user in other activities
 **/
public class MapController {
    private static final int gpsGap = 5;

    /**
     * Draw Polyline
     */
    public static void drawPolyline(Context context, List<LatLng> pointList, int color, GoogleMap mMap) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(context.getResources().getColor(color));
        polylineOptions.addAll(pointList);
        polylineOptions.width(12);
        polylineOptions.startCap(new RoundCap());
        polylineOptions.endCap(new RoundCap());
        mMap.addPolyline(polylineOptions);
    }

    /**
     * Camera bound set according to 2 LatLng points
     */
    public static void setCameraBounds(LatLng myPosition, LatLng destination, GoogleMap mMap) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(myPosition);
        builder.include(destination);
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 75);
        mMap.animateCamera(cameraUpdate);
    }

    /**
     * Calculate distance between 2 LatLng points
     */
    public static double getDistance(LatLng point1, LatLng point2) {
        double p = 0.017453292519943295;
        double a = 0.5 - cos((point2.latitude - point1.latitude) * p) / 2 +
                cos(point1.latitude * p) * cos(point2.latitude * p) *
                        (1 - cos((point2.longitude - point1.longitude) * p)) / 2;

        return 12.742 * Math.asin(Math.sqrt(a)) * 1000 * 1000;
    }

    /**
     * Map marker icon according to String and return int
     */
    public static int mapMarkerIcon(String type) {
        switch (type) {
            case "Accident":
                return R.drawable.accident_pin;
            case "Closure":
                return R.drawable.closure_pin;
            case "Flood":
                return R.drawable.flood_pin;
            case "Hazard":
                return R.drawable.hazard_pin;
            case "Landslip":
                return R.drawable.landslip_pin;
            case "Traffic-Jam":
                return R.drawable.traffic_pin;
        }
        return 0;
    }

    /** Set Icons to each static incidents**/
    public static int mapStaticIcon(String type) {
        switch (type) {
            case "Black-Spot":
                return R.drawable.accident;
            case "Traffic":
                return R.drawable.accident;
            case "Speed":
                return R.drawable.accident;
            case "Critical":
                return R.drawable.accident;
        }
        return 0;
    }

    /**Generate distance as km or m**/
    public static String generateDistanceString(double Distance) {
        String distance = "";
        if (Distance >= 1000) {
            distance += String.format("%.0f", (Distance / 1000)) + " km ";
            Distance %= 1000;
        }
        if (Distance < 1000)
            distance += String.format("%.0f", Distance) + " m ";
        return distance;
    }

    public static String generateSimpleDistanceString(double Distance) {
        String distance = "";
        if (Distance >= 1000) {
            distance += String.format("%.0f", (Distance / 1000)) + " km ";
            Distance %= 1000;
        } else if (Distance < 1000)
            distance += String.format("%.0f", Distance) + " m ";
        return distance;
    }

    /**Generate time as hours or minutes**/
    public static String generateTimeString(double Time) {
        String time = "";
        if (Time >= 3600) {
            time += String.format("%.0f", (Time / 3600)) + " H ";
            Time %= 3600;
        }

        if (Time >= 60) {
            time += String.format("%.0f", (Time / 60)) + " m ";
            Time %= 60;
        }

        if (Time < 60)
            time += String.format("%.0f", Time) + " s ";

        return time;
    }

    public static String generateSimpleTimeString(double Time) {
        double time_val = Time;
        String time = "";
        if (time_val >= 3600) {
            time += String.format("%.0f", (time_val / 3600)) + " H ";
            time_val %= 3600;
        } else if (time_val >= 60) {
            time += String.format("%.0f", (time_val / 60)) + " m ";
            time_val %= 60;
        } else if (time_val < 60)
            time += String.format("%.0f", time_val) + " s ";

        return time;
    }

    /**get speed as kmph**/
    public static String generateSpeedString(double Speed) {
        String speed = "";
        //Speed *=(18/5.0);
        speed += String.format("%.2f", Speed) + " kmph";
        return speed;
    }

    /**
     * Get LatLng point list and -> process -> return same distance point list
     */
    public static List<LatLng> generateContinuousPath(List<LatLng> pointList) {
        List<LatLng> continuousList = new ArrayList<>();
        LinkedList<LatLng> pointQueue = new LinkedList<>(pointList);
        continuousList.add(pointQueue.poll());

        while (!pointQueue.isEmpty()) {
            if (getDistance(continuousList.get(continuousList.size() - 1), pointQueue.peek()) > gpsGap)
                continuousList.add(generateNewLatLng(continuousList.get(continuousList.size() - 1), pointQueue.peek()));
            else if (getDistance(continuousList.get(continuousList.size() - 1), pointQueue.peek()) < gpsGap)
                pointQueue.poll();
            else
                continuousList.add(pointQueue.poll());
        }

        return continuousList;
    }

    private static LatLng generateNewLatLng(LatLng start, LatLng end) {
        LatLng newPoint;
        double distance = getDistance(start, end);
        double lat = ((gpsGap * end.latitude) + ((distance - gpsGap) * start.latitude)) / distance;
        double lon = ((gpsGap * end.longitude) + ((distance - gpsGap) * start.longitude)) / distance;
        newPoint = new LatLng(lat, lon);
        return newPoint;
    }

    /**calculate bearing using nearest two Location objects**/
    public static float CalculateBearingAngle(Location startPoint, Location endPoint) {

        double startLatitude = startPoint.getLatitude();
        double startLongitude = startPoint.getLongitude();
        double endLatitude = endPoint.getLatitude();
        double endLongitude = endPoint.getLongitude();

        double Phi1 = Math.toRadians(startLatitude);
        double Phi2 = Math.toRadians(endLatitude);
        double DeltaLambda = Math.toRadians(endLongitude - startLongitude);

        double Theta = Math.atan2((sin(DeltaLambda) * cos(Phi2)), (cos(Phi1) * sin(Phi2) - sin(Phi1) * cos(Phi2) * cos(DeltaLambda)));
        return (float) Math.toDegrees(Theta);
    }

    /**Date format**/
    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

}
