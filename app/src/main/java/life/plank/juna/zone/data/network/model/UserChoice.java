package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/10/2017.
 */


public class UserChoice {

    private Integer id;
    private Integer homeTeamScore;
    private Integer visitingTeamScore;
    private Integer points;
    private JunaUser junaUser;
    private FootballTeam teamSelection;
    private FootballMatch footballMatch;
    private Boolean isWinner;
    private Integer livesRemaining;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public Integer getVisitingTeamScore() {
        return visitingTeamScore;
    }

    public void setVisitingTeamScore(Integer visitingTeamScore) {
        this.visitingTeamScore = visitingTeamScore;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public JunaUser getJunaUser() {
        return junaUser;
    }

    public void setJunaUser(JunaUser junaUser) {
        this.junaUser = junaUser;
    }

    public FootballTeam getFootballTeam() {
        return teamSelection;
    }

    public void setFootballTeam(FootballTeam footballTeam) {
        this.teamSelection = footballTeam;
    }

    public FootballMatch getFootballMatch() {
        return footballMatch;
    }

    public void setFootballMatch(FootballMatch footballMatch) {
        this.footballMatch = footballMatch;
    }

    public Boolean getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(Boolean isWinner) {
        this.isWinner = isWinner;
    }

    public Integer getLivesRemaining() {
        return livesRemaining;
    }

    public void setLivesRemaining(Integer livesRemaining) {
        this.livesRemaining = livesRemaining;
    }
}