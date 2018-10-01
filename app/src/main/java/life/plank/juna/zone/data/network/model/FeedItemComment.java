package life.plank.juna.zone.data.network.model;

import java.util.List;

import lombok.Data;

@Data
public class FeedItemComment {
    private String id;
    private String message;
    private String commenterDisplayName;
    private String commenterProfilePicUrl;
    private long likeCount;
    private boolean hasLiked;
    private long replyCount;
    private List<FeedItemCommentReply> replies;
}
