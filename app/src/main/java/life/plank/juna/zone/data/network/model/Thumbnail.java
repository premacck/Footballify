package life.plank.juna.zone.data.network.model;
/**
 * Created by plank-arfaa on 06/02/18.
 */

public class Thumbnail {
    private String imageUrl;
    private int imageHeight;
    private int imageWidth;

    public Thumbnail() {
    }

    public Thumbnail(String imageUrl, int imageHeight, int imageWidth) {
        this.imageUrl = imageUrl;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }
}
