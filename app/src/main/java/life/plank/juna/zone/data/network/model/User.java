package life.plank.juna.zone.data.network.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class User {
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
    private ArrayList userPreferences;
}
