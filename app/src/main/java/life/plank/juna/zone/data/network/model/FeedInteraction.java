package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class FeedInteraction {
    private boolean hasLiked;
    private boolean hasDisliked;
    private boolean hasShared;
    private boolean hasCommented;
    private boolean hasPinned;
    private String pinId;
}
