package life.plank.juna.zone.data.network.model.firebaseModel;

import java.util.HashMap;
import java.util.Map;

public class Thumbnail {

    private String imageUrl;
    private Integer height;
    private Integer width;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Thumbnail(String imageUrl, Integer height, Integer width) {
        this.imageUrl = imageUrl;
        this.height = height;
        this.width = width;
    }

    @Override
    public String toString() {
        return "Thumbnail{" +
                "imageUrl='" + imageUrl + '\'' +
                ", height=" + height +
                ", width=" + width +
                '}';
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
