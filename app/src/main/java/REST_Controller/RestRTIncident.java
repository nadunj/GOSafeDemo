package REST_Controller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RestRTIncident {
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("accidentType")
    @Expose
    private String accidentType;
    @SerializedName("accidentDesc")
    @Expose
    private String accidentDesc;

    public RestRTIncident(Double latitude, Double longitude, String date, String accidentType, String accidentDesc) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.accidentType = accidentType;
        this.accidentDesc = accidentDesc;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccidentType() {
        return accidentType;
    }

    public void setAccidentType(String accidentType) {
        this.accidentType = accidentType;
    }

    public String getAccidentDesc() {
        return accidentDesc;
    }

    public void setAccidentDesc(String accidentDesc) {
        this.accidentDesc = accidentDesc;
    }
}
