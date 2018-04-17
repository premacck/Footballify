package life.plank.juna.zone.data.network.model;

public class MatchListModel {
    private int id;
    private int foreignId;
    private int homeGoals;
    private int awayGoals;
    private boolean hasExtraTime;
    private String startDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getForeignId() {
        return foreignId;
    }

    public void setForeignId(int foreignId) {
        this.foreignId = foreignId;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    public boolean isHasExtraTime() {
        return hasExtraTime;
    }

    public void setHasExtraTime(boolean hasExtraTime) {
        this.hasExtraTime = hasExtraTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
