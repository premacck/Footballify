package life.plank.juna.zone.data.network.model;

import java.util.Date;

import lombok.Data;

@Data
public class MatchFixture {
    private Integer id;
    private Long foreignId;
    private FootballTeam homeTeam;
    private FootballTeam awayTeam;
    private Integer matchDay;
    private Integer homeGoals;
    private Integer awayGoals;
    private String hometeamFormation;
    private String awayteamFormation;
    private Integer homeTeamPenaltyScore;
    private Integer awayTeamPenaltyScore;
    private String timeStatus;
    private Integer minute;
    private Integer extraMinute;
    private Date matchStartTime;

    /**
     * Constructor required for converting {@link MatchDetails} class to {@link MatchFixture} class.
     * TODO : remove this constructor and "from()" method below after migration from MatchFixture to MatchDetails i
     */
    public MatchFixture(Integer id, Long foreignId, FootballTeam homeTeam, FootballTeam awayTeam, Integer matchDay,
                        Integer homeGoals, Integer awayGoals, String hometeamFormation, String awayteamFormation,
                        Integer homeTeamPenaltyScore, Integer awayTeamPenaltyScore, String timeStatus,
                        Integer minute, Integer extraMinute, Date matchStartTime) {
        this.id = id;
        this.foreignId = foreignId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchDay = matchDay;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.hometeamFormation = hometeamFormation;
        this.awayteamFormation = awayteamFormation;
        this.homeTeamPenaltyScore = homeTeamPenaltyScore;
        this.awayTeamPenaltyScore = awayTeamPenaltyScore;
        this.timeStatus = timeStatus;
        this.minute = minute;
        this.extraMinute = extraMinute;
        this.matchStartTime = matchStartTime;
    }

    public static MatchFixture from(MatchDetails matchDetails) {
        return new MatchFixture(
                matchDetails.getId(),
                matchDetails.getForeignId(),
                matchDetails.getHomeTeam(),
                matchDetails.getAwayTeam(),
                matchDetails.getMatchDay(),
                matchDetails.getHomeGoals(),
                matchDetails.getAwayGoals(),
                matchDetails.getHometeamFormation(),
                matchDetails.getAwayteamFormation(),
                matchDetails.getHomeTeamPenaltyScore(),
                matchDetails.getAwayTeamPenaltyScore(),
                matchDetails.getTimeStatus(),
                matchDetails.getMinute(),
                matchDetails.getExtraMinute(),
                matchDetails.getMatchStartTime()
        );
    }
}