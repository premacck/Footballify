package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-niraj on 06-02-2018.
 */

public class ScrubberViewData {

    private String message;
    private int type;
    private LiveFeedTileData liveFeedTileData;
    private boolean triggerEvents = false;

    public ScrubberViewData(String message, int type, LiveFeedTileData liveFeedTileData, boolean triggerEvents) {
        this.message = message;
        this.type = type;
        this.liveFeedTileData = liveFeedTileData;
        this.triggerEvents = triggerEvents;
    }

    public boolean isTriggerEvents() {
        return triggerEvents;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }

    public LiveFeedTileData getLiveFeedTileData() {
        return liveFeedTileData;
    }

}
