package life.plank.juna.zone.data.network.model;

import java.util.List;

import lombok.Data;

@Data
public class UserFeed {
    public Activity activity;
    public FeedItem feedItem;

    @Data
    public class Activity {
        public String actor;
        public String verb;
        public String object;
        public String target;
        public String foreignId;
        public String time;
        public List<Object> domainEvents = null;
        public String id;
    }

    @Data
    public class FeedItem {
        public String id;
        public String title;
        public String url;
        public String source;
        public String datePublished;
        public String summary;
        public Thumbnail thumbnail;
        public List<Object> tags = null;
        public String dateCreated;
        public Object description;
        public String contentType;
        public Object comments;
        public Object interactions;
    }
}
