package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/3/2017.
 */

public class User {

    private static User user = null;
    private String username;
    private String password;

    private User() {

    }

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User withPassword(String password) {
        this.password = password;
        return this;
    }
}