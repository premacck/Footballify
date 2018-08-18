package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class MatchEvent {

    private Integer id;
    private Long foreignId;
    private FootballTeam team;
    private Boolean isHomeTeam;
    private Integer teamRef;
    private Integer footballMatchRef;
    private String eventType;
    private Integer playerRef;
    private String playerName;
    private Integer relatedPlayerRef;
    private String relatedPlayerName;
    private Integer minute;
    private Integer extraMinute;
    private Boolean injured;
    private String reason;
    private String result;
}
