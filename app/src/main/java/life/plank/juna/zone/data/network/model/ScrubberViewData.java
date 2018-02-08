package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-niraj on 06-02-2018.
 */

public class ScrubberViewData {

    String message;
    int type;
    LiveFeedTileData liveFeedTileData;

    public ScrubberViewData(String message, int type, LiveFeedTileData liveFeedTileData) {
        this.message = message;
        this.type = type;
        this.liveFeedTileData = liveFeedTileData;
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
