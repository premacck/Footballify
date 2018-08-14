package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class TeamStatsModel {
    private Integer id;
    private Integer teamRef;
    private FootballTeam footballTeam;
    private Integer seasonRef;
    private Integer homeWins;
    private Integer awayWins;
    private Integer totalWins;
    private Integer homeDraws;
    private Integer awayDraws;
    private Integer totalDraws;
    private Integer homeLosses;
    private Integer awayLosses;
    private Integer totalLosses;
    private Integer homeGoalsFor;
    private Integer awayGoalsFor;
    private Integer totalGoalsFor;
    private Integer homeGoalsAgainst;
    private Integer awayGoalsAgainst;
    private Integer totalGoalsAgainst;
    private Integer homeCleanSheet;
    private Integer awayCleanSheet;
    private String scoringMinutes;
    private Integer totalMinutes;
    private Double percentage;
    private Double totalAverageGoalsScoredPerGame;
    private Double homeAverageGoalsScoredPerGame;
    private Double awayAverageGoalsScoredPerGame;
    private Double totalAverageGoalsConcededPerGame;
    private Double homeAverageGoalsConcededPerGame;
    private Double awayAverageGoalsConcededPerGame;
    private String totalAverageFirstGoalScored;
    private String homeAverageFirstGoalScored;
    private String awayAverageFirstGoalScored;
    private String totalAverageFirstGoalConceded;
    private String homeAverageFirstGoalConceded;
    private String awayAverageFirstGoalConceded;
    private Integer homeFailedToScore;
    private Integer awayFailedToScore;
    private Integer trackingState;
    private String entityIdentifier;
}