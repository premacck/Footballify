package life.plank.juna.zone.util;

import java.util.List;

import life.plank.juna.zone.data.network.model.UserChoice;

/**
 * Created by plank-arfaa on 26/12/17.
 */

public class GlobalVariable {
    //These constants will be used to get particular data from Display Metrics
    static final int DISPLAY_HEIGHT = 100;
    static final int DISPLAY_WIDTH = 101;
    static final int SCRUBBER_VIEW_PROGRESS = 2;
    static final int SCRUBBER_VIEW_HALF_TIME = 1;
    static final int SCRUBBER_VIEW_GOAL = 3;
    static final int SCRUBBER_VIEW_SUBSTITUTE = 5;
    static final int SCRUBBER_VIEW_CARDS = 6;
    static final int SCRUBBER_VIEW_CURSOR = 4;
    static final int SCRUBBER_PRE_MATCH = 60;
    static final int SCRUBBER_POST_MATCH = 60;
    static final int SCRUBBER_VIEW_TOTAL_WINDOW = 105 + SCRUBBER_PRE_MATCH + SCRUBBER_POST_MATCH;
    private static GlobalVariable instance;
    private static int halfDuration = 5;
    private final int DISPLAY_METRICS_ERROR_STATE = -1;
    private String teamName;
    private Integer clubPointsGameRound = 0;
    private Integer clubPointsGameScore = 0;
    private Boolean isClubPointsWinner;
    private Boolean isClubGamesDraw = false;
    private List<UserChoice> userChoice;
    private String scrubberHalf = "half";
    private String scrubberNormal = "normal";
    private String scrubberGoal = "goal";
    private String scrubberCursor = "cursor";
    private String scrubberSubstitute = "substitute";
    private String scrubberCard = "card";
    private String scrubberPost = "post";

    private GlobalVariable() {
    }

    public static int getScrubberViewCursor() {
        return SCRUBBER_VIEW_CURSOR;
    }

    public static int getScrubberPreMatch() {
        return SCRUBBER_PRE_MATCH;
    }

    public static int getScrubberPostMatch() {
        return SCRUBBER_POST_MATCH;
    }

    public static int getHalfDuration() {
        return halfDuration;
    }

    public static int getScrubberViewProgress() {
        return SCRUBBER_VIEW_PROGRESS;
    }

    public static int getScrubberViewHalfTime() {
        return SCRUBBER_VIEW_HALF_TIME;
    }

    public static int getScrubberViewGoal() {
        return SCRUBBER_VIEW_GOAL;
    }

    public static int getScrubberViewSubstitute() {
        return SCRUBBER_VIEW_SUBSTITUTE;
    }

    public static int getScrubberViewCards() {
        return SCRUBBER_VIEW_CARDS;
    }

    public static int getScrubberViewTotalWindow() {
        return SCRUBBER_VIEW_TOTAL_WINDOW;
    }

    public static synchronized GlobalVariable getInstance() {
        if (instance == null) {
            instance = new GlobalVariable();
        }
        return instance;
    }

    public String getScrubberSubstitute() {
        return scrubberSubstitute;
    }

    public String getScrubberCard() {
        return scrubberCard;
    }

    public String getScrubberCursor() {
        return scrubberCursor;
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

    public int getTotalWindow() {
        return SCRUBBER_VIEW_TOTAL_WINDOW;
    }

    public String getScrubberHalf() {
        return scrubberHalf;
    }

    public String getScrubberNormal() {
        return scrubberNormal;
    }

    public String getScrubberGoal() {
        return scrubberGoal;
    }

    public String getScrubberPost() {
        return scrubberPost;
    }
}