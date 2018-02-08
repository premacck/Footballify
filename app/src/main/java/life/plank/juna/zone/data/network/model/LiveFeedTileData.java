package life.plank.juna.zone.data.network.model;

import java.util.ArrayList;

/**
 * Created by plank-niraj on 06-02-2018.
 */

public class LiveFeedTileData {
    ArrayList<Integer> images;

    public LiveFeedTileData(ArrayList<Integer> images) {
        this.images = images;
    }

    public ArrayList<Integer> getImages() {
        return images;
    }

    public void setImages(ArrayList<Integer> images) {
        this.images = images;
    }


}
