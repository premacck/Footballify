package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/4/2017.
 */

public class Player {

    private Integer id;
    private String username;
    private String provider;

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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}