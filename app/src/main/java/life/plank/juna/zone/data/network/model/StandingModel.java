package life.plank.juna.zone.data.network.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class StandingModel {
    private String footballTeam;
    private Integer position;
    private Integer gamesPlayed;
    private Integer gamesWon;
    private Integer gamesDrawn;
    private Integer gamesLost;
    private Integer goalsFor;
    private Integer goalsAgainst;
    private Integer points;

}
