package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/3/2017.
 */

public class JunaUser {

    private static JunaUser user = null;
    private Integer id;
    private String username;
    private String password;

    public static JunaUser getInstance() {
        if (user == null) {
            user = new JunaUser();
        }
        return user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JunaUser withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JunaUser withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JunaUser withPassword(String password) {
        this.password = password;
        return this;
    }
}