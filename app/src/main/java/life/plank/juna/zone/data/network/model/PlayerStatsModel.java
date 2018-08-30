package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class PlayerStatsModel {

    private int id;
    private String playerName;
    private String footballTeamLogo;
    private int minutes;
    private int goal;
    private int assist;
    private int yellowCard;
    private int yellowred;
    private int redCard;
}