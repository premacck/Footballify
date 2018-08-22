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

    public LiveScoreData(String timeStatus, int minute) {
        this.timeStatus = timeStatus;
        this.minute = minute;
    }
}