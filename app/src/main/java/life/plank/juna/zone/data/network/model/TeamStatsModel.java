package life.plank.juna.zone.data.network.model;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeamRef() {
        return teamRef;
    }

    public void setTeamRef(Integer teamRef) {
        this.teamRef = teamRef;
    }

    public FootballTeam getFootballTeam() {
        return footballTeam;
    }

    public void setFootballTeam(FootballTeam footballTeam) {
        this.footballTeam = footballTeam;
    }

    public Integer getSeasonRef() {
        return seasonRef;
    }

    public void setSeasonRef(Integer seasonRef) {
        this.seasonRef = seasonRef;
    }

    public Integer getHomeWins() {
        return homeWins;
    }

    public void setHomeWins(Integer homeWins) {
        this.homeWins = homeWins;
    }

    public Integer getAwayWins() {
        return awayWins;
    }

    public void setAwayWins(Integer awayWins) {
        this.awayWins = awayWins;
    }

    public Integer getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(Integer totalWins) {
        this.totalWins = totalWins;
    }

    public Integer getHomeDraws() {
        return homeDraws;
    }

    public void setHomeDraws(Integer homeDraws) {
        this.homeDraws = homeDraws;
    }

    public Integer getAwayDraws() {
        return awayDraws;
    }

    public void setAwayDraws(Integer awayDraws) {
        this.awayDraws = awayDraws;
    }

    public Integer getTotalDraws() {
        return totalDraws;
    }

    public void setTotalDraws(Integer totalDraws) {
        this.totalDraws = totalDraws;
    }

    public Integer getHomeLosses() {
        return homeLosses;
    }

    public void setHomeLosses(Integer homeLosses) {
        this.homeLosses = homeLosses;
    }

    public Integer getAwayLosses() {
        return awayLosses;
    }

    public void setAwayLosses(Integer awayLosses) {
        this.awayLosses = awayLosses;
    }

    public Integer getTotalLosses() {
        return totalLosses;
    }

    public void setTotalLosses(Integer totalLosses) {
        this.totalLosses = totalLosses;
    }

    public Integer getHomeGoalsFor() {
        return homeGoalsFor;
    }

    public void setHomeGoalsFor(Integer homeGoalsFor) {
        this.homeGoalsFor = homeGoalsFor;
    }

    public Integer getAwayGoalsFor() {
        return awayGoalsFor;
    }

    public void setAwayGoalsFor(Integer awayGoalsFor) {
        this.awayGoalsFor = awayGoalsFor;
    }

    public Integer getTotalGoalsFor() {
        return totalGoalsFor;
    }

    public void setTotalGoalsFor(Integer totalGoalsFor) {
        this.totalGoalsFor = totalGoalsFor;
    }

    public Integer getHomeGoalsAgainst() {
        return homeGoalsAgainst;
    }

    public void setHomeGoalsAgainst(Integer homeGoalsAgainst) {
        this.homeGoalsAgainst = homeGoalsAgainst;
    }

    public Integer getAwayGoalsAgainst() {
        return awayGoalsAgainst;
    }

    public void setAwayGoalsAgainst(Integer awayGoalsAgainst) {
        this.awayGoalsAgainst = awayGoalsAgainst;
    }

    public Integer getTotalGoalsAgainst() {
        return totalGoalsAgainst;
    }

    public void setTotalGoalsAgainst(Integer totalGoalsAgainst) {
        this.totalGoalsAgainst = totalGoalsAgainst;
    }

    public Integer getHomeCleanSheet() {
        return homeCleanSheet;
    }

    public void setHomeCleanSheet(Integer homeCleanSheet) {
        this.homeCleanSheet = homeCleanSheet;
    }

    public Integer getAwayCleanSheet() {
        return awayCleanSheet;
    }

    public void setAwayCleanSheet(Integer awayCleanSheet) {
        this.awayCleanSheet = awayCleanSheet;
    }

    public String getScoringMinutes() {
        return scoringMinutes;
    }

    public void setScoringMinutes(String scoringMinutes) {
        this.scoringMinutes = scoringMinutes;
    }

    public Integer getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Integer totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getTotalAverageGoalsScoredPerGame() {
        return totalAverageGoalsScoredPerGame;
    }

    public void setTotalAverageGoalsScoredPerGame(Double totalAverageGoalsScoredPerGame) {
        this.totalAverageGoalsScoredPerGame = totalAverageGoalsScoredPerGame;
    }

    public Double getHomeAverageGoalsScoredPerGame() {
        return homeAverageGoalsScoredPerGame;
    }

    public void setHomeAverageGoalsScoredPerGame(Double homeAverageGoalsScoredPerGame) {
        this.homeAverageGoalsScoredPerGame = homeAverageGoalsScoredPerGame;
    }

    public Double getAwayAverageGoalsScoredPerGame() {
        return awayAverageGoalsScoredPerGame;
    }

    public void setAwayAverageGoalsScoredPerGame(Double awayAverageGoalsScoredPerGame) {
        this.awayAverageGoalsScoredPerGame = awayAverageGoalsScoredPerGame;
    }

    public Double getTotalAverageGoalsConcededPerGame() {
        return totalAverageGoalsConcededPerGame;
    }

    public void setTotalAverageGoalsConcededPerGame(Double totalAverageGoalsConcededPerGame) {
        this.totalAverageGoalsConcededPerGame = totalAverageGoalsConcededPerGame;
    }

    public Double getHomeAverageGoalsConcededPerGame() {
        return homeAverageGoalsConcededPerGame;
    }

    public void setHomeAverageGoalsConcededPerGame(Double homeAverageGoalsConcededPerGame) {
        this.homeAverageGoalsConcededPerGame = homeAverageGoalsConcededPerGame;
    }

    public Double getAwayAverageGoalsConcededPerGame() {
        return awayAverageGoalsConcededPerGame;
    }

    public void setAwayAverageGoalsConcededPerGame(Double awayAverageGoalsConcededPerGame) {
        this.awayAverageGoalsConcededPerGame = awayAverageGoalsConcededPerGame;
    }

    public String getTotalAverageFirstGoalScored() {
        return totalAverageFirstGoalScored;
    }

    public void setTotalAverageFirstGoalScored(String totalAverageFirstGoalScored) {
        this.totalAverageFirstGoalScored = totalAverageFirstGoalScored;
    }

    public String getHomeAverageFirstGoalScored() {
        return homeAverageFirstGoalScored;
    }

    public void setHomeAverageFirstGoalScored(String homeAverageFirstGoalScored) {
        this.homeAverageFirstGoalScored = homeAverageFirstGoalScored;
    }

    public String getAwayAverageFirstGoalScored() {
        return awayAverageFirstGoalScored;
    }

    public void setAwayAverageFirstGoalScored(String awayAverageFirstGoalScored) {
        this.awayAverageFirstGoalScored = awayAverageFirstGoalScored;
    }

    public String getTotalAverageFirstGoalConceded() {
        return totalAverageFirstGoalConceded;
    }

    public void setTotalAverageFirstGoalConceded(String totalAverageFirstGoalConceded) {
        this.totalAverageFirstGoalConceded = totalAverageFirstGoalConceded;
    }

    public String getHomeAverageFirstGoalConceded() {
        return homeAverageFirstGoalConceded;
    }

    public void setHomeAverageFirstGoalConceded(String homeAverageFirstGoalConceded) {
        this.homeAverageFirstGoalConceded = homeAverageFirstGoalConceded;
    }

    public String getAwayAverageFirstGoalConceded() {
        return awayAverageFirstGoalConceded;
    }

    public void setAwayAverageFirstGoalConceded(String awayAverageFirstGoalConceded) {
        this.awayAverageFirstGoalConceded = awayAverageFirstGoalConceded;
    }

    public Integer getHomeFailedToScore() {
        return homeFailedToScore;
    }

    public void setHomeFailedToScore(Integer homeFailedToScore) {
        this.homeFailedToScore = homeFailedToScore;
    }

    public Integer getAwayFailedToScore() {
        return awayFailedToScore;
    }

    public void setAwayFailedToScore(Integer awayFailedToScore) {
        this.awayFailedToScore = awayFailedToScore;
    }

    public Integer getTrackingState() {
        return trackingState;
    }

    public void setTrackingState(Integer trackingState) {
        this.trackingState = trackingState;
    }

    public String getEntityIdentifier() {
        return entityIdentifier;
    }

    public void setEntityIdentifier(String entityIdentifier) {
        this.entityIdentifier = entityIdentifier;

    }

    public class FootballTeam {
        private Integer id;
        private Integer foreignId;
        private String name;
        private Integer countryRef;
        private Boolean isNationalTeam;
        private Integer founded;
        private Integer homeStadiumRef;
        private String logoLink;
        private Integer trackingState;
        private String entityIdentifier;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getForeignId() {
            return foreignId;
        }

        public void setForeignId(Integer foreignId) {
            this.foreignId = foreignId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCountryRef() {
            return countryRef;
        }

        public void setCountryRef(Integer countryRef) {
            this.countryRef = countryRef;
        }

        public Boolean getIsNationalTeam() {
            return isNationalTeam;
        }

        public void setIsNationalTeam(Boolean isNationalTeam) {
            this.isNationalTeam = isNationalTeam;
        }

        public Integer getFounded() {
            return founded;
        }

        public void setFounded(Integer founded) {
            this.founded = founded;
        }

        public Integer getHomeStadiumRef() {
            return homeStadiumRef;
        }

        public void setHomeStadiumRef(Integer homeStadiumRef) {
            this.homeStadiumRef = homeStadiumRef;
        }

        public String getLogoLink() {
            return logoLink;
        }

        public void setLogoLink(String logoLink) {
            this.logoLink = logoLink;
        }

        public Integer getTrackingState() {
            return trackingState;
        }

        public void setTrackingState(Integer trackingState) {
            this.trackingState = trackingState;
        }

        public String getEntityIdentifier() {
            return entityIdentifier;
        }

        public void setEntityIdentifier(String entityIdentifier) {
            this.entityIdentifier = entityIdentifier;
        }
    }
}