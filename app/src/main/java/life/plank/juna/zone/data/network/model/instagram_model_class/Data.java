package life.plank.juna.zone.data.network.model.instagram_model_class;

/**
 * Created by plank-sobia on 11/24/2017.
 */

public class Data {
    private String id;
    private String username;
    private String profile_picture;
    private String full_name;
    private String bio;
    private String website;
    private Boolean is_business;
    private Counts counts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profile_picture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profile_picture = profilePicture;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getIsBusiness() {
        return is_business;
    }

    public void setIsBusiness(Boolean isBusiness) {
        this.is_business = isBusiness;
    }

    public Counts getCounts() {
        return counts;
    }

    public void setCounts(Counts counts) {
        this.counts = counts;
    }
}
