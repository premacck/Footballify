package life.plank.juna.zone.util;

import java.util.List;

import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.UserChoice;

/**
 * Created by plank-arfaa on 26/12/17.
 */

public class GlobalVariable {
    //These constants will be used to get particular data from Display Metrics
    static final int DISPLAY_HEIGHT = 100;
    static final int DISPLAY_WIDTH = 101;

    private static GlobalVariable instance;
    private static int halfDuration = 5;
    private final int DISPLAY_METRICS_ERROR_STATE = -1;
    private String teamName;
    private Integer clubPointsGameRound = 0;
    private Integer clubPointsGameScore = 0;
    private Boolean isClubPointsWinner;
    private Boolean isClubGamesDraw = false;
    private List<UserChoice> userChoice;

    private List<FootballFeed> footballFeeds;

    private GlobalVariable() {
    }


    public static synchronized GlobalVariable getInstance() {
        if (instance == null) {
            instance = new GlobalVariable();
        }
        return instance;
    }


    public List<FootballFeed> getFootballFeeds() {
        return footballFeeds;
    }

    public void setFootballFeeds(List<FootballFeed> footballFeeds) {
        this.footballFeeds = footballFeeds;
    }

    public int getDisplayHeight() {
        return DISPLAY_HEIGHT;
    }

    public int getDisplayWidth() {
        return DISPLAY_WIDTH;
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

    public List<UserChoice> getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(List<UserChoice> userChoice) {
        this.userChoice = userChoice;
    }

    public int getDisplayMetricsErrorState() {
        return DISPLAY_METRICS_ERROR_STATE;
    }


}