package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class SignInModel {
    private String objectId;
    private String displayName;
    private String emailAddress;
    private String country;
    private String city;
    private String identityProvider;
    private String givenName;
    private String surname;
    private String id;

}
