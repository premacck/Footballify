package life.plank.juna.zone.data.network.model;

import lombok.Data;

/**
 * Created by plank-arfaa on 06/02/18.
 */

@Data
public class FeedItem {
    private String id;
    private String title;
    private String url;
    private String source;
    private String datePublished;
    private String summary;
    private Thumbnail thumbnail;
    private String dateCreated;
    private String contentType;
    private Interaction interactions;
    private String seasonName;
    private String countryName;
    private String description;
    private User actor;
    /** Field to store the tile width of the feed item */
    private int tileWidth;

    public FeedItem() {
    }

    public FeedItem(String id, String title, String url, String source, String datePublished, String summary, Thumbnail thumbnail, String dateCreated, String contentType, Interaction interactions) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.source = source;
        this.datePublished = datePublished;
        this.summary = summary;
        this.thumbnail = thumbnail;
        this.dateCreated = dateCreated;
        this.contentType = contentType;
        this.interactions = interactions;
    }

}