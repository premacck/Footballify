package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-hasan on 2/21/2018.
 */

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getSticker() {
        return sticker;
    }

    public void setSticker(int sticker) {
        this.sticker = sticker;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
}
