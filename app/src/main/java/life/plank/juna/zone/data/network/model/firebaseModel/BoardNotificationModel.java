package life.plank.juna.zone.data.network.model.firebaseModel;

import java.util.List;

public class BoardNotificationModel {
    private String title;
    private String url;
    private Object source;
    private String datePublished;
    private Object summary;
    private Thumbnail thumbnail;
    private List<Object> tags = null;
    private String dateCreated;
    private String contentType;
    private Object interactions;
    private String id;
    private String type;
    private String rid;
    private String self;
    private String etag;
    private String attachments;
    private Integer ts;

    @Override
    public String toString() {
        return "BoardNotificationModel{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", source=" + source +
                ", datePublished='" + datePublished + '\'' +
                ", summary=" + summary +
                ", thumbnail=" + thumbnail +
                ", tags=" + tags +
                ", dateCreated='" + dateCreated + '\'' +
                ", contentType='" + contentType + '\'' +
                ", interactions=" + interactions +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", rid='" + rid + '\'' +
                ", self='" + self + '\'' +
                ", etag='" + etag + '\'' +
                ", attachments='" + attachments + '\'' +
                ", ts=" + ts +
                '}';
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

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

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

    public Object getInteractions() {
        return interactions;
    }

    public void setInteractions(Object interactions) {
        this.interactions = interactions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Integer getTs() {
        return ts;
    }

    public void setTs(Integer ts) {
        this.ts = ts;
    }

}
