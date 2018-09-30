package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class FeedInteraction {
    private Boolean hasLiked;
    private Boolean hasDisliked;
    private Boolean hasShared;
    private Boolean hasCommented;
    private Boolean hasPinned;

}
