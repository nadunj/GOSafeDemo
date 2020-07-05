package apps.njl.gosafe.RoomDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Trip_Table")
public class Trip {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "route")
    private String route;

    @ColumnInfo(name = "startTime")
    private String dateTime;


    @ColumnInfo(name = "earnedScore")
    private int earned_score;

    @ColumnInfo(name = "reducedScore")
    private int reduced_score;

    @ColumnInfo(name = "total_score")
    private int total_score;

    @ColumnInfo(name = "speedMarkers")
    private String speedMarkerList;

    @ColumnInfo(name = "totalDuration")
    private double totalDuration;

    @ColumnInfo(name = "totalDistance")
    private double totalDistance;

    @ColumnInfo(name = "averageSpeed")
    private double averageSpeed;

    public Trip() {
    }

    @Ignore
    public Trip(int id, String route, String dateTime, int earned_score, int reduced_score, int total_score, String speedMarkerList, double totalDuration, double totalDistance, double averageSpeed) {
        this.id = id;
        this.route = route;
        this.dateTime = dateTime;
        this.earned_score = earned_score;
        this.reduced_score = reduced_score;
        this.total_score = total_score;
        this.speedMarkerList = speedMarkerList;
        this.totalDuration = totalDuration;
        this.totalDistance = totalDistance;
        this.averageSpeed = averageSpeed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getEarned_score() {
        return earned_score;
    }

    public void setEarned_score(int earned_score) {
        this.earned_score = earned_score;
    }

    public int getReduced_score() {
        return reduced_score;
    }

    public void setReduced_score(int reduced_score) {
        this.reduced_score = reduced_score;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public String getSpeedMarkerList() {
        return speedMarkerList;
    }

    public void setSpeedMarkerList(String speedMarkerList) {
        this.speedMarkerList = speedMarkerList;
    }

    public double getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(double totalDuration) {
        this.totalDuration = totalDuration;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }
}
