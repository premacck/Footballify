package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-niraj on 17-02-2018.
 */

public class LiveZoneMatchListData {

    String homeTeam;
    String visitingTeam;
    int homeTeamScore;
    int visitingTeamScore;
    String commentary;

    public LiveZoneMatchListData(String homeTeam, String visitingTeam, int homeTeamScore, int visitingTeamScore, String commentary) {
        this.homeTeam = homeTeam;
        this.visitingTeam = visitingTeam;
        this.homeTeamScore = homeTeamScore;
        this.visitingTeamScore = visitingTeamScore;
        this.commentary = commentary;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getVisitingTeam() {
        return visitingTeam;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public int getVisitingTeamScore() {
        return visitingTeamScore;
    }

    public String getCommentary() {
        return commentary;
    }
}
