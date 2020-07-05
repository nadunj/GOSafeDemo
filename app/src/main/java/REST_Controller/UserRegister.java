package REST_Controller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRegister {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phonenumber")
    @Expose
    private Integer phonenumber;
    @SerializedName("nic")
    @Expose
    private String nic;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("licenseUrl")
    @Expose
    private String licenseUrl;
    @SerializedName("dateOfIssueLicense")
    @Expose
    private String dateOfIssueLicense;
    @SerializedName("dateOfExpireLicense")
    @Expose
    private String dateOfExpireLicense;
    @SerializedName("imageOfDriverUrl")
    @Expose
    private String imageOfDriverUrl;
    @SerializedName("licenseNum")
    @Expose
    private String license_num;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLicense_num() {
        return license_num;
    }

    public void setLicense_num(String license_num) {
        this.license_num = license_num;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(Integer phonenumber) {
        this.phonenumber = phonenumber;
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

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
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

}
