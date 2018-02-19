package life.plank.juna.zone.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FootballFeed {
    private int reactionType = -1;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("Source")
    @Expose
    private String source;
    @SerializedName("DatePublished")
    @Expose
    private String datePublished;
    @SerializedName("Summary")
    @Expose
    private String summary;
    @SerializedName("Thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("Tags")
    @Expose
    private Object tags;
    @SerializedName("DateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("ContentType")
    @Expose
    private String contentType;
    @SerializedName("DomainEvents")
    @Expose

    private List<Object> domainEvents = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Object getTags() {
        return tags;
    }

    public void setTags(Object tags) {
        this.tags = tags;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<Object> getDomainEvents() {
        return domainEvents;
    }

    public void setDomainEvents(List<Object> domainEvents) {
        this.domainEvents = domainEvents;
    }

    public int getReactionType() {
        return reactionType;
    }

    public void setReactionType(int reactionType) {
        this.reactionType = reactionType;
    }


}
