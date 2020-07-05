package apps.njl.gosafe;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Gayan Lakshitha on 4/16/2018.
 */

public class RouteInfo {
    private LatLng start_point;
    private LatLng destination;
    private List<LatLng> points;
    private String endLocation;
    private int distance;
    private int duration;

    public RouteInfo() {
    }


    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public RouteInfo(LatLng start_point, LatLng destination, List<LatLng> points, String endLocation, int distance, int duration) {
        this.start_point = start_point;
        this.destination = destination;
        this.points = points;
        this.endLocation = endLocation;
        this.distance = distance;
        this.duration = duration;

    }

    public LatLng getStart_point() {
        return start_point;
    }

    public void setStart_point(LatLng start_point) {
        this.start_point = start_point;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
