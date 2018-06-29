package life.plank.juna.zone.data.network.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by plank-arfaa on 06/02/18.
 */

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Interactions getInteractions() {
        return interactions;
    }

    public void setInteractions(Interactions interactions) {
        this.interactions = interactions;
    }

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
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }

        public Integer getShares() {
            return shares;
        }

        public void setShares(Integer shares) {
            this.shares = shares;
        }

        public Integer getPins() {
            return pins;
        }

        public void setPins(Integer pins) {
            this.pins = pins;
        }

        public Integer getComments() {
            return comments;
        }

        public void setComments(Integer comments) {
            this.comments = comments;
        }

        public Integer getPosts() {
            return posts;
        }

        public void setPosts(Integer posts) {
            this.posts = posts;
        }

        public Integer getBlocks() {
            return blocks;
        }

        public void setBlocks(Integer blocks) {
            this.blocks = blocks;
        }

        public Integer getBans() {
            return bans;
        }

        public void setBans(Integer bans) {
            this.bans = bans;
        }

        public Integer getMutes() {
            return mutes;
        }

        public void setMutes(Integer mutes) {
            this.mutes = mutes;
        }

        public Integer getReports() {
            return reports;
        }

        public void setReports(Integer reports) {
            this.reports = reports;
        }

        public class Thumbnail {

            private String imageUrl;
            private Integer height;
            private Integer width;
            private Map<String, Object> additionalProperties = new HashMap<String, Object>();

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public Integer getHeight() {
                return height;
            }

            public void setHeight(Integer height) {
                this.height = height;
            }

            public Integer getWidth() {
                return width;
            }

            public void setWidth(Integer width) {
                this.width = width;
            }
        }
    }

}