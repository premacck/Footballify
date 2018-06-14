package life.plank.juna.zone.data.network.model.firebaseModel;

public class NotificationFeedItem {

    private String datePublished;
    private Object summary;
    private Thumbnail Thumbnail;
    private String dateCreated;
    private String id;
    private String title;
    private String ContentType;
    private String Url;

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public Object getSummary() {
        return summary;
    }

    public void setSummary(Object summary) {
        this.summary = summary;
    }

    public Thumbnail getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

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

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
