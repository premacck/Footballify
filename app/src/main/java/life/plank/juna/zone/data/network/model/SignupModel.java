package life.plank.juna.zone.data.network.model;

public class SignupModel {
    private String objectId;
    private String displayName;
    private String emailAddress;
    private String country;
    private String city;
    private String identityProvider;
    private String givenName;
    private String surname;

    public SignupModel(String objectId,String displayName, String emailAddress, String country, String city, String identityProvider, String givenName, String surname) {

        this.objectId = objectId;
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.country = country;
        this.city = city;
        this.identityProvider = identityProvider;
        this.givenName = givenName;
        this.surname = surname;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIdentityProvider() {
        return identityProvider;
    }

    public void setIdentityProvider(String identityProvider) {
        this.identityProvider = identityProvider;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


}


