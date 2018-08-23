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
    private LiveScoreData liveScoreData;

    public MatchEvent(LiveScoreData liveScoreData) {
        this.liveScoreData = liveScoreData;
    }
}
