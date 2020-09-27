package apps.njl.gosafe;

public class Users {
    private String username;
    private String firstname;
    private String lastname;
    private String residence;
    private String email;

    public Users(){

    }
    public Users(String username, String firstname, String lastname, String residence, String email) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.residence = residence;
        this.email = email;
    }
}
