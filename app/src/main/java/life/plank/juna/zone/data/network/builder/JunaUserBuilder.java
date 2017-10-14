package life.plank.juna.zone.data.network.builder;

import life.plank.juna.zone.data.network.model.JunaUser;

/**
 * Created by plank-sobia on 10/12/2017.
 */

public class JunaUserBuilder {
    private JunaUser junaUser = null;
    private static JunaUserBuilder junaUserBuilder = null;

    private JunaUserBuilder() {
        junaUser = new JunaUser();
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
        return this;
    }

    public JunaUserBuilder withPassword(String password) {
        junaUser.setPassword(password);
        return this;
    }

    public JunaUser build() {
        return junaUser;
    }
}
