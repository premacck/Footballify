package life.plank.juna.zone.viewmodel;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plank-prachi on 2/21/2018.
 */
public class StandingFeedModel {
    String teamName;
    String played;
    String win;
    String draw;
    String lost;
    String goalDifference;
    String pointTable;

    public StandingFeedModel(String teamName, String played, String win, String draw, String lost, String goalDifference, String pointTable) {
        this.teamName = teamName;
        this.played = played;
        this.win = win;
        this.draw = draw;
        this.lost = lost;
        this.goalDifference = goalDifference;
        this.pointTable = pointTable;
    }

    public static List<StandingFeedModel> getStandingData(Context context) {
        List<StandingFeedModel> standingFeedModelList = new ArrayList<>();
        standingFeedModelList.add(new StandingFeedModel("Man City", "27", "23", "31", "1", "59", "72"));
        standingFeedModelList.add(new StandingFeedModel("Man Utd", "47", "23", "31", "1", "59", "72"));
        standingFeedModelList.add(new StandingFeedModel("Liver Pool", "67", "23", "31", "1", "59", "72"));
        standingFeedModelList.add(new StandingFeedModel("Chelesa", "27", "23", "31", "1", "9", "72"));
        standingFeedModelList.add(new StandingFeedModel("Tottenham", "97", "23", "31", "1", "19", "72"));
        standingFeedModelList.add(new StandingFeedModel("Arsenal", "7", "3", "1", "3", "9", "2"));
        standingFeedModelList.add(new StandingFeedModel("Burnela", "27", "23", "31", "1", "39", "72"));
        standingFeedModelList.add(new StandingFeedModel("Leisester", "27", "23", "31", "1", "59", "72"));
        standingFeedModelList.add(new StandingFeedModel("Everton", "27", "23", "31", "1", "59", "72"));
        standingFeedModelList.add(new StandingFeedModel("Beurnomath", "27", "23", "31", "1", "59", "72"));
        return standingFeedModelList;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPlayed() {
        return played;
    }

    public void setPlayed(String played) {
        this.played = played;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getLost() {
        return lost;
    }

    public void setLost(String lost) {
        this.lost = lost;
    }

    public String getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(String goalDifference) {
        this.goalDifference = goalDifference;
    }

    public String getPointTable() {
        return pointTable;
    }

    public void setPointTable(String pointTable) {
        this.pointTable = pointTable;
    }
}