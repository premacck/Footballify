package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class StandingModel {
    private FootballTeam footballTeam;
    private Integer position;
    private Integer gamesPlayed;
    private Integer gamesWon;
    private Integer gamesDrawn;
    private Integer gamesLost;
    private Integer goalsFor;
    private Integer goalsAgainst;
    private Integer points;

}
