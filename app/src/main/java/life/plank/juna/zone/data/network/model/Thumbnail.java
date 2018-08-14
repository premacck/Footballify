package life.plank.juna.zone.data.network.model;

import lombok.Data;

/**
 * Created by plank-arfaa on 06/02/18.
 */

@Data
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

}
