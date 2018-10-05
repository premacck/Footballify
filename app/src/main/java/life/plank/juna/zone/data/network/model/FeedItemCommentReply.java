package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class FeedItemCommentReply {
    private String id;
    private final String message;
    private String commenterDisplayName;
    private String commenterProfilePicUrl;
}