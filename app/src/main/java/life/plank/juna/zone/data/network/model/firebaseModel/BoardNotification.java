package life.plank.juna.zone.data.network.model.firebaseModel;


import lombok.Data;

@Data
public class BoardNotification {

    private String contentType;
    private String thumbnailImageUrl;
    private Integer thumbnailWidth;
    private Integer thumbnailHeight;
    private String imageUrl;
    private String action;
    private String actor;
    private String title;
    private long foreignId;
}