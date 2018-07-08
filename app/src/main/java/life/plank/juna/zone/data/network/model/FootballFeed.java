package life.plank.juna.zone.data.network.model;

import lombok.Data;

/**
 * Created by plank-arfaa on 06/02/18.
 */

@Data
public class FootballFeed {
    private String id;
    private String title;
    private String url;
    private String source;
    private String datePublished;
    private String summary;
    private Thumbnail thumbnail;
    private String dateCreated;
    private String contentType;
    private Interactions interactions;

    public FootballFeed() {
    }

    public FootballFeed(String id, String title, String url, String source, String datePublished, String summary, Thumbnail thumbnail, String dateCreated, String contentType, Interactions interactions) {
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

    @Data
    public class Interactions {
        private Integer likes;
        private Integer shares;
        private Integer pins;
        private Integer comments;
        private Integer posts;
        private Integer blocks;
        private Integer bans;
        private Integer mutes;
        private Integer reports;
    }

}