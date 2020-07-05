package REST_Controller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StaticIncidentData {
    @SerializedName("blackSpots")
    @Expose
    private List<Blackspot> blackSpots = null;
    @SerializedName("criticalPoints")
    @Expose
    private List<CriticalLocation> criticalPoints = null;
    @SerializedName("roadSigns")
    @Expose
    private List<TrafficSign> roadSigns = null;
    @SerializedName("speedLimits")
    @Expose
    private List<SpeedLimit> speedLimits = null;

    public List<Blackspot> getBlackSpots() {
        return blackSpots;
    }

    public void setBlackSpots(List<Blackspot> blackSpots) {
        this.blackSpots = blackSpots;
    }

    public List<CriticalLocation> getCriticalPoints() {
        return criticalPoints;
    }

    public void setCriticalPoints(List<CriticalLocation> criticalPoints) {
        this.criticalPoints = criticalPoints;
    }

    public List<TrafficSign> getRoadSigns() {
        return roadSigns;
    }

    public void setRoadSigns(List<TrafficSign> roadSigns) {
        this.roadSigns = roadSigns;
    }

    public List<SpeedLimit> getSpeedLimits() {
        return speedLimits;
    }

    public void setSpeedLimits(List<SpeedLimit> speedLimits) {
        this.speedLimits = speedLimits;
    }

}
