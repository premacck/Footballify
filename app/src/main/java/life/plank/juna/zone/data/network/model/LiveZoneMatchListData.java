package life.plank.juna.zone.data.network.model;

import lombok.Data;

/**
 * Created by plank-niraj on 17-02-2018.
 */

@Data
public class LiveZoneMatchListData {

    private String homeTeam;
    private String visitingTeam;
    private int homeTeamScore;
    private int visitingTeamScore;
    private String commentary;

    public LiveZoneMatchListData(String homeTeam, String visitingTeam, int homeTeamScore, int visitingTeamScore, String commentary) {
        this.homeTeam = homeTeam;
        this.visitingTeam = visitingTeam;
        this.homeTeamScore = homeTeamScore;
        this.visitingTeamScore = visitingTeamScore;
        this.commentary = commentary;
    }

}
