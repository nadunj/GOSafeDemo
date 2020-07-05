package REST_Controller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("nic")
    @Expose
    private String nic;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("isConfirmed")
    @Expose
    private Integer isConfirmed;
    @SerializedName("dateOfIssueLicense")
    @Expose
    private String dateOfIssueLicense;
    @SerializedName("dateOfExpireLicense")
    @Expose
    private String dateOfExpireLicense;
    @SerializedName("imageOfDriverUrl")
    @Expose
    private String imageOfDriverUrl;
    @SerializedName("keycloakId")
    @Expose
    private String keycloakId;
    @SerializedName("licenseNum")
    @Expose
    private String licenseNum;
    @SerializedName("idUrl")
    @Expose
    private String idUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Integer getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Integer isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getDateOfIssueLicense() {
        return dateOfIssueLicense;
    }

    public void setDateOfIssueLicense(String dateOfIssueLicense) {
        this.dateOfIssueLicense = dateOfIssueLicense;
    }

    public String getDateOfExpireLicense() {
        return dateOfExpireLicense;
    }

    public void setDateOfExpireLicense(String dateOfExpireLicense) {
        this.dateOfExpireLicense = dateOfExpireLicense;
    }

    public String getImageOfDriverUrl() {
        return imageOfDriverUrl;
    }

    public void setImageOfDriverUrl(String imageOfDriverUrl) {
        this.imageOfDriverUrl = imageOfDriverUrl;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public String getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
    }

    public String getIdUrl() {
        return idUrl;
    }

    public void setIdUrl(String idUrl) {
        this.idUrl = idUrl;
    }
}
