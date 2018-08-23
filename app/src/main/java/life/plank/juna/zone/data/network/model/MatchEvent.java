package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class MatchEvent {
    private Boolean isHomeTeam;
    private String eventType;
    private String playerName;
    private String relatedPlayerName;
    private Integer minute;
    private Integer extraMinute;
    private Boolean injured;
    private String reason;
    private String result;
    private LiveTimeStatus liveTimeStatus;

    /**
     * Constructor to add whistle events (kick-off, half-time, full-time etc.) in football timeline.
     */
    public MatchEvent(LiveTimeStatus liveTimeStatus) {
        this.liveTimeStatus = liveTimeStatus;
    }
}
