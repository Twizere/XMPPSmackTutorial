package thirdrdhand.smacktutorial.activities.auth;

/**
 * Created by pacit on 2017/09/07.
 */

public class AuthUser {
    public String Username;
    public String Password;
    public String dateTime;
    public String isAdmin;

    public AuthUser(String username, String password) {
        Username = username;
        Password = password;
    }
}
