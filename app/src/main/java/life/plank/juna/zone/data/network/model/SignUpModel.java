package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class SignUpModel {
    private String objectId;
    private String displayName;
    private String emailAddress;
    private String country;
    private String city;
    private String identityProvider;
    private String givenName;
    private String surname;

    public SignUpModel(String objectId, String displayName, String emailAddress, String country, String city, String identityProvider, String givenName, String surname) {

        this.objectId = objectId;
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.country = country;
        this.city = city;
        this.identityProvider = identityProvider;
        this.givenName = givenName;
        this.surname = surname;
    }

}