package life.plank.juna.zone.data.network.model;
import java.util.List;

public class FootballFeed {
    private String Id;
    private String Title;
    private String Url;
    private String Source;
    private String DatePublished;
    private String Summary;
    private Thumbnail Thumbnail;
    private Object Tags;
    private String DateCreated;
    private String ContentType;
    private List<Object> DomainEvents = null;
    private int reactionType = -1;
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        this.Source = source;
    }

    public String getDatePublished() {
        return DatePublished;
    }

    public void setDatePublished(String datePublished) {
        this.DatePublished = datePublished;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String  summary) {
        this.Summary = summary;
    }

    public Thumbnail getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.Thumbnail = thumbnail;
    }

    public Object getTags() {
        return Tags;
    }

    public void setTags(Object tags) {
        this.Tags = tags;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.DateCreated = dateCreated;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        this.ContentType = contentType;
    }

    public List<Object> getDomainEvents() {
        return DomainEvents;
    }

    public void setDomainEvents(List<Object> domainEvents) {
        this.DomainEvents = domainEvents;
    }

    public int getReactionType() {
        return reactionType;
    }

    public void setReactionType(int reactionType) {
        this.reactionType = reactionType;
    }
}