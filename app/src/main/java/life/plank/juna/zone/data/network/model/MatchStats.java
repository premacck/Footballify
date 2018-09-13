package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class MatchStats {
    private int homeShots;
    private int homeShotsOnTarget;
    private int homePossession;
    private int homeFouls;
    private int homeRedCards;
    private int homeYellowCards;
    private int homeOffsides;
    private int homeCorners;
    private int awayShots;
    private int awayShotsOnTarget;
    private int awayPossession;
    private int awayFouls;
    private int awayRedCards;
    private int awayYellowCards;
    private int awayOffsides;
    private int awayCorners;
    private int errorMessage;
}