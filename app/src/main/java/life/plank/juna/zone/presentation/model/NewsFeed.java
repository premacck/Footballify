package life.plank.juna.zone.presentation.model;

/**
 * Created by plank-sobia on 9/4/2017.
 */

public class NewsFeed {

    private int newsFeedImage;
    private String newsFeedTitle;

    public NewsFeed(int newsFeedImage, String newsFeedTitle) {
        this.newsFeedImage = newsFeedImage;
        this.newsFeedTitle = newsFeedTitle;
    }

    public int getNewsFeedImage() {
        return newsFeedImage;
    }

    public void setNewsFeedImage(int newsFeedImage) {
        this.newsFeedImage = newsFeedImage;
    }

    public String getNewsFeedTitle() {
        return newsFeedTitle;
    }

    public void setNewsFeedTitle(String newsFeedTitle) {
        this.newsFeedTitle = newsFeedTitle;
    }
}
