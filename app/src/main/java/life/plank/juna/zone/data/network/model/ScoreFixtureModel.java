package life.plank.juna.zone.data.network.model;


public class ScoreFixtureModel {

    private Integer id;
    private Integer foreignId;
    private League league;
    private Integer leagueRef;
    private Integer seasonRef;
    private HomeTeam homeTeam;
    private Integer homeTeamRef;
    private AwayTeam awayTeam;
    private Integer awayTeamRef;
    private Integer venueRef;
    private Integer homeGoals;
    private Integer awayGoals;
    private Boolean hasExtraTime;
    private String startDate;
    private Boolean commentaries;
    private Boolean winningOddsCalculated;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Integer homeTeamPenaltyScore;
    private Integer awayTeamPenaltyScore;
    private String halfTimeScore;
    private String timeStatus;
    private Integer minute;
    private Integer extraMinute;
    private Integer injuryTime;
    private String matchStartTime;
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

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Integer getLeagueRef() {
        return leagueRef;
    }

    public void setLeagueRef(Integer leagueRef) {
        this.leagueRef = leagueRef;
    }

    public Integer getSeasonRef() {
        return seasonRef;
    }

    public void setSeasonRef(Integer seasonRef) {
        this.seasonRef = seasonRef;
    }

    public HomeTeam getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(HomeTeam homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Integer getHomeTeamRef() {
        return homeTeamRef;
    }

    public void setHomeTeamRef(Integer homeTeamRef) {
        this.homeTeamRef = homeTeamRef;
    }

    public AwayTeam getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(AwayTeam awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Integer getAwayTeamRef() {
        return awayTeamRef;
    }

    public void setAwayTeamRef(Integer awayTeamRef) {
        this.awayTeamRef = awayTeamRef;
    }

    public Integer getVenueRef() {
        return venueRef;
    }

    public void setVenueRef(Integer venueRef) {
        this.venueRef = venueRef;
    }

    public Integer getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(Integer homeGoals) {
        this.homeGoals = homeGoals;
    }

    public Integer getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(Integer awayGoals) {
        this.awayGoals = awayGoals;
    }

    public Boolean getHasExtraTime() {
        return hasExtraTime;
    }

    public void setHasExtraTime(Boolean hasExtraTime) {
        this.hasExtraTime = hasExtraTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Boolean getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(Boolean commentaries) {
        this.commentaries = commentaries;
    }

    public Boolean getWinningOddsCalculated() {
        return winningOddsCalculated;
    }

    public void setWinningOddsCalculated(Boolean winningOddsCalculated) {
        this.winningOddsCalculated = winningOddsCalculated;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public Integer getHomeTeamPenaltyScore() {
        return homeTeamPenaltyScore;
    }

    public void setHomeTeamPenaltyScore(Integer homeTeamPenaltyScore) {
        this.homeTeamPenaltyScore = homeTeamPenaltyScore;
    }

    public Integer getAwayTeamPenaltyScore() {
        return awayTeamPenaltyScore;
    }

    public void setAwayTeamPenaltyScore(Integer awayTeamPenaltyScore) {
        this.awayTeamPenaltyScore = awayTeamPenaltyScore;
    }

    public String getHalfTimeScore() {
        return halfTimeScore;
    }

    public void setHalfTimeScore(String halfTimeScore) {
        this.halfTimeScore = halfTimeScore;
    }

    public String getTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(String timeStatus) {
        this.timeStatus = timeStatus;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getExtraMinute() {
        return extraMinute;
    }

    public void setExtraMinute(Integer extraMinute) {
        this.extraMinute = extraMinute;
    }

    public Integer getInjuryTime() {
        return injuryTime;
    }

    public void setInjuryTime(Integer injuryTime) {
        this.injuryTime = injuryTime;
    }

    public String getMatchStartTime() {
        return matchStartTime;
    }

    public void setMatchStartTime(String matchStartTime) {
        this.matchStartTime = matchStartTime;
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

    public class League {

        private Integer id;
        private Integer foreignId;
        private String name;
        private Boolean isCup;
        private Integer countryRef;
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

        public Boolean getIsCup() {
            return isCup;
        }

        public void setIsCup(Boolean isCup) {
            this.isCup = isCup;
        }

        public Integer getCountryRef() {
            return countryRef;
        }

        public void setCountryRef(Integer countryRef) {
            this.countryRef = countryRef;
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

    public class HomeTeam {
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

    public class AwayTeam {

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
