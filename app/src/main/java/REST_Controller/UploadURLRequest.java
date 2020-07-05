package REST_Controller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadURLRequest {
    @SerializedName("idUrl")
    @Expose
    private String idUrl;
    @SerializedName("kid")
    @Expose
    private String kid;
    @SerializedName("imageOfDriverUrl")
    @Expose
    private String imageOfDriverUrl;

    public UploadURLRequest(String idUrl, String kid, String imageOfDriverUrl) {
        this.idUrl = idUrl;
        this.kid = kid;
        this.imageOfDriverUrl = imageOfDriverUrl;
    }

    public String getIdUrl() {
        return idUrl;
    }

    public void setIdUrl(String idUrl) {
        this.idUrl = idUrl;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getImageOfDriverUrl() {
        return imageOfDriverUrl;
    }

    public void setImageOfDriverUrl(String imageOfDriverUrl) {
        this.imageOfDriverUrl = imageOfDriverUrl;
    }
}
