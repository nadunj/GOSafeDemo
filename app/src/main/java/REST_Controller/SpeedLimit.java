package REST_Controller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpeedLimit {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("speedLimit")
    @Expose
    private Integer speedLimit;
    @SerializedName("thresholdLimit")
    @Expose
    private Integer thresholdLimit;
    @SerializedName("radius")
    @Expose
    private Integer radius;
    @SerializedName("message")
    @Expose
    private String message;

    public SpeedLimit(Double latitude, Double longitude, Integer speedLimit, Integer thresholdLimit, Integer radius, String message) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speedLimit = speedLimit;
        this.thresholdLimit = thresholdLimit;
        this.radius = radius;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Integer speedLimit) {
        this.speedLimit = speedLimit;
    }

    public Integer getThresholdLimit() {
        return thresholdLimit;
    }

    public void setThresholdLimit(Integer thresholdLimit) {
        this.thresholdLimit = thresholdLimit;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
