package life.plank.juna.zone.data.network.builder;

import life.plank.juna.zone.data.network.model.JunaUser;

/**
 * Created by plank-sobia on 10/12/2017.
 */

public class JunaUserBuilder {
    private String userName;
    private JunaUser junaUser = null;
    private static JunaUserBuilder junaUserBuilder = null;

    private JunaUserBuilder() {
        junaUser = new JunaUser();
    }

    public String getUserName() {
        return userName;
    }

    public static JunaUserBuilder getInstance() {
        if (junaUserBuilder == null)
            junaUserBuilder = new JunaUserBuilder();
        return junaUserBuilder;
    }

    public JunaUserBuilder withId(Integer id) {
        junaUser.setId(id);
        return this;
    }

    public JunaUserBuilder withUserName(String userName) {
        junaUser.setUsername(userName);
        this.userName = userName;
        return this;
    }

    public JunaUserBuilder withPassword(String password) {
        junaUser.setPassword(password);
        return this;
    }

    public JunaUserBuilder withDisplayName(String displayName) {
        junaUser.setDisplayName(displayName);
        return this;
    }

    public JunaUserBuilder withProvider(String provider) {
        junaUser.setProvider(provider);
        return this;
    }

    public JunaUser build() {
        return junaUser;
    }
}
