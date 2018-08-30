package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class StandingModel {
    private String teamName;
    private Integer position;
    private String footballTeamLogo;
    private Integer matchesPlayed;
    private Integer wins;
    private Integer draws;
    private Integer losses;
    private Integer goalsFor;
    private Integer goalsAgainst;
    private Integer goalDifference;
    private Integer points;
}