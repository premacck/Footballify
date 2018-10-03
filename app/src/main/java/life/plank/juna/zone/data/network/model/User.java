package life.plank.juna.zone.data.network.model;

import java.util.List;

import lombok.Data;

@Data
public class User {
    public List<UserPreference> userPreferences;
    private String objectId;
    private String displayName;
    private String emailAddress;
    private String country;
    private String city;
    private String profilePictureUrl;
    private String identityProvider;
    private String givenName;
    private String surname;
    private String id;
}
