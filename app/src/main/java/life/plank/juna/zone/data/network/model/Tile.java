package life.plank.juna.zone.data.network.model;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import lombok.Data;

/**
 * Created by plank-hasan on 2/21/2018.
 */

@Data
public class Tile {
    private String tag;
    private int image;
    private int sticker;
    private String tweet;

    public Tile(String tag, int image, int sticker, String tweet) {
        this.tag = tag;
        this.image = image;
        this.sticker = sticker;
        this.tweet = tweet;
    }

    public static List<Tile> getTiles() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("image", R.drawable.image0, R.drawable.ic_sticker_four, ""));
        tiles.add(new Tile("text", 0, 0, "Why would Mourinho do that? Isn't he done with"));
        tiles.add(new Tile("sticker", R.drawable.ic_club_list_background, R.drawable.ic_sticker_two, "Why would Mourinho do that? Isn't he done with"));
        return tiles;
    }
}
