package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class TeamStatsModel {
    private String teamName;
    private String footballTeamLogo;
    private long win;
    private long loss;
    private long goal;
    private long pass;
    private long shot;
    private long yellowCard;
    private long redCard;
}