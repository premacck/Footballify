package life.plank.juna.zone.data.network.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//todo: streamline this model in next pull request
public class ScoreFixtureModel {

    private Integer id;
    private Long foreignId;
    private League league;
    private Integer leagueRef;
    private Integer seasonRef;
    private HomeTeam homeTeam;
    private Integer homeTeamRef;
    private AwayTeam awayTeam;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getForeignId() {
        return foreignId;
    }

    public void setForeignId(Long foreignId) {
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

    public List<MatchEvent> getMatchEvents() {
        return matchEvents;
    }

    public void setMatchEvents(List<MatchEvent> matchEvents) {
        this.matchEvents = matchEvents;
    }

    public Integer getHomeTeamMatchSummaryRef() {
        return homeTeamMatchSummaryRef;
    }

    public void setHomeTeamMatchSummaryRef(Integer homeTeamMatchSummaryRef) {
        this.homeTeamMatchSummaryRef = homeTeamMatchSummaryRef;
    }

    public Integer getAwayTeamMatchSummaryRef() {
        return awayTeamMatchSummaryRef;
    }

    public void setAwayTeamMatchSummaryRef(Integer awayTeamMatchSummaryRef) {
        this.awayTeamMatchSummaryRef = awayTeamMatchSummaryRef;
    }

    public Integer getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(Integer matchDay) {
        this.matchDay = matchDay;
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

    public void setStartDate(String startDateString) {
        try {
            this.startDate = startDateString;// DateUtil.getIsoFormattedDate(startDateString);
        } catch (Exception e) {
            this.startDate = null;
        }
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

    public String getHometeamFormation() {
        return hometeamFormation;
    }

    public void setHometeamFormation(String hometeamFormation) {
        this.hometeamFormation = hometeamFormation;
    }

    public String getAwayteamFormation() {
        return awayteamFormation;
    }

    public void setAwayteamFormation(String awayteamFormation) {
        this.awayteamFormation = awayteamFormation;
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

    public String getFullTimeScore() {
        return fullTimeScore;
    }

    public void setFullTimeScore(String fullTimeScore) {
        this.fullTimeScore = fullTimeScore;
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

    public Date getMatchStartTime() {
        return matchStartTime;
    }

    public void setMatchStartTime(Date matchStartTime) {
        this.matchStartTime = matchStartTime;
    }

    public class AwayTeam {

        private Integer id;
        private Long foreignId;
        private String name;
        private Integer countryRef;
        private Boolean isNationalTeam;
        private Integer founded;
        private Integer homeStadiumRef;
        private String logoLink;
        private Integer trackingState;
        private String entityIdentifier;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Long getForeignId() {
            return foreignId;
        }

        public void setForeignId(Long foreignId) {
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

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put( name, value );
        }

    }

    public class HomeTeam {

        private Integer id;
        private Long foreignId;
        private String name;
        private Integer countryRef;
        private Boolean isNationalTeam;
        private Integer founded;
        private Integer homeStadiumRef;
        private String logoLink;
        private Integer trackingState;
        private String entityIdentifier;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Long getForeignId() {
            return foreignId;
        }

        public void setForeignId(Long foreignId) {
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

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put( name, value );
        }

    }

    public class League {

        private Integer id;
        private Long foreignId;
        private String name;
        private Boolean isCup;
        private Integer countryRef;
        private Integer trackingState;
        private String entityIdentifier;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Long getForeignId() {
            return foreignId;
        }

        public void setForeignId(Long foreignId) {
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

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put( name, value );
        }

    }

    public class MatchEvent {

        private Integer id;
        private Long foreignId;
        private Team team;
        private Integer teamRef;
        private Integer footballMatchRef;
        private String eventType;
        private Integer playerRef;
        private String playerName;
        private Integer relatedPlayerRef;
        private Integer minute;
        private Integer extraMinute;
        private Boolean injured;
        private Integer trackingState;
        private String entityIdentifier;
        private String reason;
        private String result;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Long getForeignId() {
            return foreignId;
        }

        public void setForeignId(Long foreignId) {
            this.foreignId = foreignId;
        }

        public Team getTeam() {
            return team;
        }

        public void setTeam(Team team) {
            this.team = team;
        }

        public Integer getTeamRef() {
            return teamRef;
        }

        public void setTeamRef(Integer teamRef) {
            this.teamRef = teamRef;
        }

        public Integer getFootballMatchRef() {
            return footballMatchRef;
        }

        public void setFootballMatchRef(Integer footballMatchRef) {
            this.footballMatchRef = footballMatchRef;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Integer getPlayerRef() {
            return playerRef;
        }

        public void setPlayerRef(Integer playerRef) {
            this.playerRef = playerRef;
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public Integer getRelatedPlayerRef() {
            return relatedPlayerRef;
        }

        public void setRelatedPlayerRef(Integer relatedPlayerRef) {
            this.relatedPlayerRef = relatedPlayerRef;
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

        public Boolean getInjured() {
            return injured;
        }

        public void setInjured(Boolean injured) {
            this.injured = injured;
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

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put( name, value );
        }
    }

    public class Team {
        private Integer id;
        private Long foreignId;
        private String name;
        private Integer countryRef;
        private Boolean isNationalTeam;
        private Integer founded;
        private Integer homeStadiumRef;
        private String logoLink;
        private Integer trackingState;
        private String entityIdentifier;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Long getForeignId() {
            return foreignId;
        }

        public void setForeignId(Long foreignId) {
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

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put( name, value );
        }
    }
}



