package life.plank.juna.zone.data.network.model;

import java.util.ArrayList;

import lombok.Data;

/**
 * Created by plank-niraj on 06-02-2018.
 */

@Data
public class LiveFeedTileData {
    private ArrayList<Tile> tiles;

    public LiveFeedTileData(ArrayList<Tile> images) {
        this.tiles = images;
    }

}
