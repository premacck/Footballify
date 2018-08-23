package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class LiveScoreData {
    private String timeStatus;
    private int minute;
    private int extraMinute;
    private int homeGoals;
    private int awayGoals;
    private int homeTeamPenaltyScore;
    private int awayTeamPenaltyScore;
    private String halfTimeScore;
    private String fullTimeScore;
    private String extraTimeScore;

    //    TODO: replace the usages of this method with the value received from backend.
    public LiveScoreData(String timeStatus, int minute) {
        this.timeStatus = timeStatus;
        this.minute = minute;
    }
}