package life.plank.juna.zone.data.network.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

//todo: streamline this model in next pull request
@Data
public class ScoreFixtureModel {

    private Integer id;
    private Long foreignId;
    private League league;
    private Integer leagueRef;
    private Integer seasonRef;
    private FootballTeam homeTeam;
    private Integer homeTeamRef;
    private FootballTeam awayTeam;
    private Integer awayTeamRef;
    private Integer venueRef;
    private List<MatchEvent> matchEvents = null;
    private Integer homeTeamMatchSummaryRef;
    private Integer awayTeamMatchSummaryRef;
    private Integer matchDay;
    private Integer homeGoals;
    private Integer awayGoals;
    private Boolean hasExtraTime;
    private String startDate;
    private Boolean commentaries;
    private Boolean winningOddsCalculated;
    private String hometeamFormation;
    private String awayteamFormation;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Integer homeTeamPenaltyScore;
    private Integer awayTeamPenaltyScore;
    private String halfTimeScore;
    private String fullTimeScore;
    private String timeStatus;
    private Integer minute;
    private Integer extraMinute;
    private Integer injuryTime;
    private Date matchStartTime;
    private Integer trackingState;

    @Data
    public class League {

        private Integer id;
        private Long foreignId;
        private String name;
        private Boolean isCup;
        private Integer countryRef;
    }

}



