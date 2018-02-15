package life.plank.juna.zone.data.network.model;

import java.util.List;

/**
 * Created by plank-arfaa on 05/02/18.
 */

public class FootballFeed {

    private int reactionType = -1;
    //TODO: Change all keys to camel case after changes are made on the backend
    private String Id;
    private String Headline;
    private String Url;
    private String Source;
    private String DatePublished;
    private String Summary;
    private Thumbnail Thumbnail;
    private List<String> Tags = null;
    private String DateCreated;

    public int getReactionType() {
        return reactionType;
    }

    public void setReactionType(int reactionType) {
        this.reactionType = reactionType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getHeadline() {
        return Headline;
    }

    public void setHeadline(String headline) {
        Headline = headline;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getDatePublished() {
        return DatePublished;
    }

    public void setDatePublished(String datePublished) {
        DatePublished = datePublished;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public Thumbnail getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        Thumbnail = thumbnail;
    }

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> tags) {
        Tags = tags;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

}
