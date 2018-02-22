package life.plank.juna.zone.data.network.model;

import java.util.ArrayList;

/**
 * Created by plank-niraj on 06-02-2018.
 */

public class LiveFeedTileData {
    private ArrayList<Tile> tiles;

    public LiveFeedTileData(ArrayList<Tile> images) {
        this.tiles = images;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }


}
