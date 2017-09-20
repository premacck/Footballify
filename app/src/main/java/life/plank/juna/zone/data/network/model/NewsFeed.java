
package life.plank.juna.zone.data.network.model;

import java.util.List;

public class NewsFeed {

    public Activity activity;
    public String title;
    public String summary;
    public String datePublished;
    public String lastUpdatedTime;
    public List<String> authors = null;
    public List<String> links = null;
    public List<String> imageUrls = null;

    public NewsFeed withActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public NewsFeed withTitle(String title) {
        this.title = title;
        return this;
    }

    public NewsFeed withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public NewsFeed withDatePublished(String datePublished) {
        this.datePublished = datePublished;
        return this;
    }

    public NewsFeed withLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
        return this;
    }

    public NewsFeed withAuthors(List<String> authors) {
        this.authors = authors;
        return this;
    }

    public NewsFeed withLinks(List<String> links) {
        this.links = links;
        return this;
    }

    public NewsFeed withImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}