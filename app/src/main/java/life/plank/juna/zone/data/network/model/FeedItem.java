package life.plank.juna.zone.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    private String description;
    @SerializedName("actor")
    @Expose
    private User user;
    List<FeedItemComment> comments;
    /** Field to store the tile width of the feed item */
    private int tileWidth;
    /**
     * Field to check whether the feed item is pinned
     * TODO: Remove once isPinned is provided in Interactions
     */
    private boolean isPinned;
    private String pinId;
    private int previousPosition;
}