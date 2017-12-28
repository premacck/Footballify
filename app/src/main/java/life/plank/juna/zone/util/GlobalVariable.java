package life.plank.juna.zone.util;

/**
 * Created by plank-arfaa on 26/12/17.
 */

public class GlobalVariable {
    private static GlobalVariable instance;

    private String teamName;
    private Integer clubPointsGameRound = 0;
    private Integer clubPointsGameScore = 0;
    private Boolean  isClubPointsWinner;
    private Boolean isClubGamesDraw = false;

    private GlobalVariable() {
    }

    public Boolean getClubGamesDraw() {
        return isClubGamesDraw;
    }

    public void setClubGamesDraw(Boolean clubGamesDraw) {
        isClubGamesDraw = clubGamesDraw;
    }

    public Boolean getClubPointsWinner() {
        return isClubPointsWinner;
    }

    public void setClubPointsWinner(Boolean clubPointsWinner) {
        isClubPointsWinner = clubPointsWinner;
    }

    public Integer getClubPointsGameRound() {
        return clubPointsGameRound;
    }

    public void setClubPointsGameRound(Integer clubPointsGameRound) {
        this.clubPointsGameRound = clubPointsGameRound;
    }

    public Integer getClubPointsGameScore() {
        return clubPointsGameScore;
    }

    public void setClubPointsGameScore(Integer clubPointsGameScore) {
        this.clubPointsGameScore = clubPointsGameScore;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public static synchronized GlobalVariable getInstance() {
        if (instance == null) {
            instance = new GlobalVariable();
        }
        return instance;
    }
}