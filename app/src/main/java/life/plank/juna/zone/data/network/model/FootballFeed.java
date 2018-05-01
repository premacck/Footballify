package life.plank.juna.zone.data.network.model;
import java.util.List;
/**
 * Created by plank-arfaa on 06/02/18.
 */

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

    public FootballFeed(String id, String title, String url, String source, String datePublished, String summary, life.plank.juna.zone.data.network.model.Thumbnail thumbnail, Object tags, String dateCreated, String contentType, List<Object> domainEvents, int reactionType) {
        Id = id;
        Title = title;
        Url = url;
        Source = source;
        DatePublished = datePublished;
        Summary = summary;
        Thumbnail = thumbnail;
        Tags = tags;
        DateCreated = dateCreated;
        ContentType = contentType;
        DomainEvents = domainEvents;
        this.reactionType = reactionType;
    }

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