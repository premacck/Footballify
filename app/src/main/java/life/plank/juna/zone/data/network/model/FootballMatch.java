package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/4/2017.
 */

public class FootballMatch {

    private Integer id;
    private String datePlayed;
    private Integer homeTeamScore;
    private Integer visitingTeamScore;
    private Integer leagueYearStart;
    private Integer weekNumber;
    private HomeTeam homeTeam;
    private VisitingTeam visitingTeam;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(String datePlayed) {
        this.datePlayed = datePlayed;
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

    public Integer getLeagueYearStart() {
        return leagueYearStart;
    }

    public void setLeagueYearStart(Integer leagueYearStart) {
        this.leagueYearStart = leagueYearStart;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public HomeTeam getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(HomeTeam homeTeam) {
        this.homeTeam = homeTeam;
    }

    public VisitingTeam getVisitingTeam() {
        return visitingTeam;
    }

    public void setVisitingTeam(VisitingTeam visitingTeam) {
        this.visitingTeam = visitingTeam;
    }
}