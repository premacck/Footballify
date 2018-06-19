package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-arfaa on 06/02/18.
 */

public class FootballFeed {
    private String id;
    private String title;
    private String url;
    private String source;
    private String datePublished;
    private String summary;
    private Thumbnail thumbnail;
    private String dateCreated;
    private String contentType;

    public FootballFeed() {
    }

    public FootballFeed(String id, String title, String url, String source, String datePublished, String summary, Thumbnail thumbnail, String dateCreated, String contentType) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.source = source;
        this.datePublished = datePublished;
        this.summary = summary;
        this.thumbnail = thumbnail;
        this.dateCreated = dateCreated;
        this.contentType = contentType;
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
}