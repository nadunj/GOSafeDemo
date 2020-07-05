package apps.njl.gosafe;

public class StaticVisualizeIncident {
    private double latitude;
    private double longitude;
    private String incident_type;
    private int radius;

    public StaticVisualizeIncident() {
    }

    public StaticVisualizeIncident(double latitude, double longitude, String incident_type, int radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.incident_type = incident_type;
        this.radius = radius;
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

    public String getIncident_type() {
        return incident_type;
    }

    public void setIncident_type(String incident_type) {
        this.incident_type = incident_type;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
