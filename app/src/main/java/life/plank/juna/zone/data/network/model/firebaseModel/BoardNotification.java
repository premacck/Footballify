package life.plank.juna.zone.data.network.model.firebaseModel;


public class BoardNotification {

    private String ContentType;
    private String ThumbnailImageUrl;
    private Integer ThumbnailWidth;
    private Integer ThumbnailHeight;
    private String ImageUrl;
    private String Action;
    private String Actor;
    private String Title;

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getThumbnailImageUrl() {
        return ThumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        ThumbnailImageUrl = thumbnailImageUrl;
    }

    public Integer getThumbnailWidth() {
        return ThumbnailWidth;
    }

    public void setThumbnailWidth(Integer thumbnailWidth) {
        ThumbnailWidth = thumbnailWidth;
    }

    public Integer getThumbnailHeight() {
        return ThumbnailHeight;
    }

    public void setThumbnailHeight(Integer thumbnailHeight) {
        ThumbnailHeight = thumbnailHeight;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public String getActor() {
        return Actor;
    }

    public void setActor(String actor) {
        Actor = actor;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}