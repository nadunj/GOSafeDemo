package apps.njl.gosafe;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class SummeryInfo {
    private LatLng start_location;
    private LatLng end_location;
    private String route;
    private String startTime;
    private long total_time;
    private int score_addIncidents;
    private int score_removeIncidents;
    private int score_overSpeed;
    private List<SpeedMarker> speedMarkerList;
    private boolean isEndJourney;

    public SummeryInfo() {
    }

    public SummeryInfo(LatLng start_location, LatLng end_location, String route, String startTime, long total_time, int score_addIncidents, int score_removeIncidents, int score_overSpeed, List<SpeedMarker> speedMarkerList, boolean isEndJourney) {
        this.start_location = start_location;
        this.end_location = end_location;
        this.route = route;
        this.startTime = startTime;
        this.total_time = total_time;
        this.score_addIncidents = score_addIncidents;
        this.score_removeIncidents = score_removeIncidents;
        this.score_overSpeed = score_overSpeed;
        this.speedMarkerList = speedMarkerList;
        this.isEndJourney = isEndJourney;
    }

    /**getters and setters**/
    public LatLng getStart_location() {
        return start_location;
    }

    public void setStart_location(LatLng start_location) {
        this.start_location = start_location;
    }

    public LatLng getEnd_location() {
        return end_location;
    }

    public void setEnd_location(LatLng end_location) {
        this.end_location = end_location;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public long getTotal_time() {
        return total_time;
    }

    public void setTotal_time(long total_time) {
        this.total_time = total_time;
    }

    public int getScore_addIncidents() {
        return score_addIncidents;
    }

    public void setScore_addIncidents(int score_addIncidents) {
        this.score_addIncidents = score_addIncidents;
    }

    public int getScore_removeIncidents() {
        return score_removeIncidents;
    }

    public void setScore_removeIncidents(int score_removeIncidents) {
        this.score_removeIncidents = score_removeIncidents;
    }

    public int getScore_overSpeed() {
        return score_overSpeed;
    }

    public void setScore_overSpeed(int score_overSpeed) {
        this.score_overSpeed = score_overSpeed;
    }

    public List<SpeedMarker> getSpeedMarkerList() {
        return speedMarkerList;
    }

    public void setSpeedMarkerList(List<SpeedMarker> speedMarkerList) {
        this.speedMarkerList = speedMarkerList;
    }

    public boolean isEndJourney() {
        return isEndJourney;
    }

    public void setEndJourney(boolean endJourney) {
        isEndJourney = endJourney;
    }
}
