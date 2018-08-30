package life.plank.juna.zone.data.network.model;

import java.util.List;

import lombok.Data;

@Data
public class MatchSummary {
    private Integer id;
    private Integer foreignId;
    private Integer leagueRef;
    private FootballTeam homeTeam;
    private FootballTeam awayTeam;
    private Integer homeGoals;
    private Integer awayGoals;
    private String halfTimeScore;
    private String fullTimeScore;
    private String hometeamFormation;
    private String awayteamFormation;
    private Boolean commentaries;
    private Stadium venue;
    private String timeStatus;
    private String matchStartTime;
    private Integer minute;
    private Integer extraMinute;
    private Integer injuryTime;
    private List<MatchEvent> matchEvents;
    private List<Commentary> commentary;
    private Integer matchDay;
    private Boolean hasExtraTime;
    private String startDate;
    private Boolean winningOddsCalculated;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Integer homeTeamPenaltyScore;
    private Integer awayTeamPenaltyScore;
}