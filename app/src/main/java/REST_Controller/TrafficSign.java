package REST_Controller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrafficSign {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("sign")
    @Expose
    private String sign;
    @SerializedName("isConfirmed")
    @Expose
    private Integer isConfirmed;
    @SerializedName("reporterId")
    @Expose
    private String reporterId;

    public TrafficSign(Double latitude, Double longitude, String sign) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sign = sign;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Integer isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }
}
