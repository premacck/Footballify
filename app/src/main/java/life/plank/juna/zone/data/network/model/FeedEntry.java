package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class FeedEntry {
    private FeedItem feedItem;
    private FeedInteraction feedInteractions;
}
