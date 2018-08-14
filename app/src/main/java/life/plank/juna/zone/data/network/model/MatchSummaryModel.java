package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class MatchSummaryModel {

    private Integer id;
    private Integer foreignId;
    private Integer leagueRef;
    private Integer seasonRef;
    private Integer homeTeamRef;
    private Integer awayTeamRef;
    private Integer venueRef;
    private TeamMatchSummary homeTeamMatchSummary;
    private Integer homeTeamMatchSummaryRef;
    private TeamMatchSummary awayTeamMatchSummary;
    private Integer awayTeamMatchSummaryRef;
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
    private String matchStartTime;

    @Data
    public class TeamMatchSummary {

        private Integer id;
        private Integer footballTeamRef;
        private Integer footballMatchRef;
        private Integer totalShots;
        private Integer onGoalShots;
        private Integer offGoalShots;
        private Integer blockedShots;
        private Integer insideBoxShots;
        private Integer outsideBoxShots;
        private Integer totalPasses;
        private Integer accuratePasses;
        private Integer accuratePassesPercentage;
        private Integer attacks;
        private Integer dangerousAttacks;
        private Integer fouls;
        private Integer corners;
        private Integer offsides;
        private Integer possessionTime;
        private Integer yellowCards;
        private Integer redCards;
        private Integer saves;
        private Integer substitutions;
        private Integer goalKick;
        private Integer goalAttempts;
        private Integer freeKick;
        private Integer throwIn;
        private Integer ballSafe;
        private Integer trackingState;
        private String entityIdentifier;

    }
}
