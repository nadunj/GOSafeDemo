package apps.njl.gosafe;

/**
 * Created by Gayan Lakshitha on 4/19/2018.
 */

public class RealtimeIncident {
    private String incident_id;
    private String incident_name;
    private String user_id;
    private String time;
    private double latitude;
    private double longitude;

    public RealtimeIncident() {
    }

    public RealtimeIncident(String incident_id, String incident_name, String user_id, String time, double latitude, double longitude) {
        this.incident_id = incident_id;
        this.incident_name = incident_name;
        this.user_id = user_id;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIncident_id() {
        return incident_id;
    }

    public void setIncident_id(String incident_id) {
        this.incident_id = incident_id;
    }

    public String getIncident_name() {
        return incident_name;
    }

    public void setIncident_name(String incident_name) {
        this.incident_name = incident_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
