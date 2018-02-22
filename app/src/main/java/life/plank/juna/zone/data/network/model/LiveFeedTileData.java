package life.plank.juna.zone.data.network.model;

import java.util.List;

/**
 * Created by plank-niraj on 06-02-2018.
 */

public class LiveFeedTileData {
    List<Integer> images;

    public LiveFeedTileData(List<Integer> images) {
        this.images = images;
    }

    public List<Integer> getImages() {
        return images;
    }

    public void setImages(List<Integer> images) {
        this.images = images;
    }


}
