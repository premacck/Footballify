package life.plank.juna.zone.data.network.model;

public class MatchSummaryModel {

        private Integer id;
        private Integer foreignId;
        private Integer leagueRef;
        private Integer seasonRef;
        private Integer homeTeamRef;
        private Integer awayTeamRef;
        private Integer venueRef;
        private HomeTeamMatchSummary homeTeamMatchSummary;
        private Integer homeTeamMatchSummaryRef;
        private AwayTeamMatchSummary awayTeamMatchSummary;
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

        public Integer getHomeTeamRef() {
            return homeTeamRef;
        }

        public void setHomeTeamRef(Integer homeTeamRef) {
            this.homeTeamRef = homeTeamRef;
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

        public HomeTeamMatchSummary getHomeTeamMatchSummary() {
            return homeTeamMatchSummary;
        }

        public void setHomeTeamMatchSummary(HomeTeamMatchSummary homeTeamMatchSummary) {
            this.homeTeamMatchSummary = homeTeamMatchSummary;
        }

        public Integer getHomeTeamMatchSummaryRef() {
            return homeTeamMatchSummaryRef;
        }

        public void setHomeTeamMatchSummaryRef(Integer homeTeamMatchSummaryRef) {
            this.homeTeamMatchSummaryRef = homeTeamMatchSummaryRef;
        }

        public AwayTeamMatchSummary getAwayTeamMatchSummary() {
            return awayTeamMatchSummary;
        }

        public void setAwayTeamMatchSummary(AwayTeamMatchSummary awayTeamMatchSummary) {
            this.awayTeamMatchSummary = awayTeamMatchSummary;
        }

        public Integer getAwayTeamMatchSummaryRef() {
            return awayTeamMatchSummaryRef;
        }

        public void setAwayTeamMatchSummaryRef(Integer awayTeamMatchSummaryRef) {
            this.awayTeamMatchSummaryRef = awayTeamMatchSummaryRef;
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

        public String getMatchStartTime() {
            return matchStartTime;
        }

        public void setMatchStartTime(String matchStartTime) {
            this.matchStartTime = matchStartTime;

        }

        public class HomeTeamMatchSummary {

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

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getFootballTeamRef() {
                return footballTeamRef;
            }

            public void setFootballTeamRef(Integer footballTeamRef) {
                this.footballTeamRef = footballTeamRef;
            }

            public Integer getFootballMatchRef() {
                return footballMatchRef;
            }

            public void setFootballMatchRef(Integer footballMatchRef) {
                this.footballMatchRef = footballMatchRef;
            }

            public Integer getTotalShots() {
                return totalShots;
            }

            public void setTotalShots(Integer totalShots) {
                this.totalShots = totalShots;
            }

            public Integer getOnGoalShots() {
                return onGoalShots;
            }

            public void setOnGoalShots(Integer onGoalShots) {
                this.onGoalShots = onGoalShots;
            }

            public Integer getOffGoalShots() {
                return offGoalShots;
            }

            public void setOffGoalShots(Integer offGoalShots) {
                this.offGoalShots = offGoalShots;
            }

            public Integer getBlockedShots() {
                return blockedShots;
            }

            public void setBlockedShots(Integer blockedShots) {
                this.blockedShots = blockedShots;
            }

            public Integer getInsideBoxShots() {
                return insideBoxShots;
            }

            public void setInsideBoxShots(Integer insideBoxShots) {
                this.insideBoxShots = insideBoxShots;
            }

            public Integer getOutsideBoxShots() {
                return outsideBoxShots;
            }

            public void setOutsideBoxShots(Integer outsideBoxShots) {
                this.outsideBoxShots = outsideBoxShots;
            }

            public Integer getTotalPasses() {
                return totalPasses;
            }

            public void setTotalPasses(Integer totalPasses) {
                this.totalPasses = totalPasses;
            }

            public Integer getAccuratePasses() {
                return accuratePasses;
            }

            public void setAccuratePasses(Integer accuratePasses) {
                this.accuratePasses = accuratePasses;
            }

            public Integer getAccuratePassesPercentage() {
                return accuratePassesPercentage;
            }

            public void setAccuratePassesPercentage(Integer accuratePassesPercentage) {
                this.accuratePassesPercentage = accuratePassesPercentage;
            }

            public Integer getAttacks() {
                return attacks;
            }

            public void setAttacks(Integer attacks) {
                this.attacks = attacks;
            }

            public Integer getDangerousAttacks() {
                return dangerousAttacks;
            }

            public void setDangerousAttacks(Integer dangerousAttacks) {
                this.dangerousAttacks = dangerousAttacks;
            }

            public Integer getFouls() {
                return fouls;
            }

            public void setFouls(Integer fouls) {
                this.fouls = fouls;
            }

            public Integer getCorners() {
                return corners;
            }

            public void setCorners(Integer corners) {
                this.corners = corners;
            }

            public Integer getOffsides() {
                return offsides;
            }

            public void setOffsides(Integer offsides) {
                this.offsides = offsides;
            }

            public Integer getPossessionTime() {
                return possessionTime;
            }

            public void setPossessionTime(Integer possessionTime) {
                this.possessionTime = possessionTime;
            }

            public Integer getYellowCards() {
                return yellowCards;
            }

            public void setYellowCards(Integer yellowCards) {
                this.yellowCards = yellowCards;
            }

            public Integer getRedCards() {
                return redCards;
            }

            public void setRedCards(Integer redCards) {
                this.redCards = redCards;
            }

            public Integer getSaves() {
                return saves;
            }

            public void setSaves(Integer saves) {
                this.saves = saves;
            }

            public Integer getSubstitutions() {
                return substitutions;
            }

            public void setSubstitutions(Integer substitutions) {
                this.substitutions = substitutions;
            }

            public Integer getGoalKick() {
                return goalKick;
            }

            public void setGoalKick(Integer goalKick) {
                this.goalKick = goalKick;
            }

            public Integer getGoalAttempts() {
                return goalAttempts;
            }

            public void setGoalAttempts(Integer goalAttempts) {
                this.goalAttempts = goalAttempts;
            }

            public Integer getFreeKick() {
                return freeKick;
            }

            public void setFreeKick(Integer freeKick) {
                this.freeKick = freeKick;
            }

            public Integer getThrowIn() {
                return throwIn;
            }

            public void setThrowIn(Integer throwIn) {
                this.throwIn = throwIn;
            }

            public Integer getBallSafe() {
                return ballSafe;
            }

            public void setBallSafe(Integer ballSafe) {
                this.ballSafe = ballSafe;
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
        public class AwayTeamMatchSummary {

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

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getFootballTeamRef() {
                return footballTeamRef;
            }

            public void setFootballTeamRef(Integer footballTeamRef) {
                this.footballTeamRef = footballTeamRef;
            }

            public Integer getFootballMatchRef() {
                return footballMatchRef;
            }

            public void setFootballMatchRef(Integer footballMatchRef) {
                this.footballMatchRef = footballMatchRef;
            }

            public Integer getTotalShots() {
                return totalShots;
            }

            public void setTotalShots(Integer totalShots) {
                this.totalShots = totalShots;
            }

            public Integer getOnGoalShots() {
                return onGoalShots;
            }

            public void setOnGoalShots(Integer onGoalShots) {
                this.onGoalShots = onGoalShots;
            }

            public Integer getOffGoalShots() {
                return offGoalShots;
            }

            public void setOffGoalShots(Integer offGoalShots) {
                this.offGoalShots = offGoalShots;
            }

            public Integer getBlockedShots() {
                return blockedShots;
            }

            public void setBlockedShots(Integer blockedShots) {
                this.blockedShots = blockedShots;
            }

            public Integer getInsideBoxShots() {
                return insideBoxShots;
            }

            public void setInsideBoxShots(Integer insideBoxShots) {
                this.insideBoxShots = insideBoxShots;
            }

            public Integer getOutsideBoxShots() {
                return outsideBoxShots;
            }

            public void setOutsideBoxShots(Integer outsideBoxShots) {
                this.outsideBoxShots = outsideBoxShots;
            }

            public Integer getTotalPasses() {
                return totalPasses;
            }

            public void setTotalPasses(Integer totalPasses) {
                this.totalPasses = totalPasses;
            }

            public Integer getAccuratePasses() {
                return accuratePasses;
            }

            public void setAccuratePasses(Integer accuratePasses) {
                this.accuratePasses = accuratePasses;
            }

            public Integer getAccuratePassesPercentage() {
                return accuratePassesPercentage;
            }

            public void setAccuratePassesPercentage(Integer accuratePassesPercentage) {
                this.accuratePassesPercentage = accuratePassesPercentage;
            }

            public Integer getAttacks() {
                return attacks;
            }

            public void setAttacks(Integer attacks) {
                this.attacks = attacks;
            }

            public Integer getDangerousAttacks() {
                return dangerousAttacks;
            }

            public void setDangerousAttacks(Integer dangerousAttacks) {
                this.dangerousAttacks = dangerousAttacks;
            }

            public Integer getFouls() {
                return fouls;
            }

            public void setFouls(Integer fouls) {
                this.fouls = fouls;
            }

            public Integer getCorners() {
                return corners;
            }

            public void setCorners(Integer corners) {
                this.corners = corners;
            }

            public Integer getOffsides() {
                return offsides;
            }

            public void setOffsides(Integer offsides) {
                this.offsides = offsides;
            }

            public Integer getPossessionTime() {
                return possessionTime;
            }

            public void setPossessionTime(Integer possessionTime) {
                this.possessionTime = possessionTime;
            }

            public Integer getYellowCards() {
                return yellowCards;
            }

            public void setYellowCards(Integer yellowCards) {
                this.yellowCards = yellowCards;
            }

            public Integer getRedCards() {
                return redCards;
            }

            public void setRedCards(Integer redCards) {
                this.redCards = redCards;
            }

            public Integer getSaves() {
                return saves;
            }

            public void setSaves(Integer saves) {
                this.saves = saves;
            }

            public Integer getSubstitutions() {
                return substitutions;
            }

            public void setSubstitutions(Integer substitutions) {
                this.substitutions = substitutions;
            }

            public Integer getGoalKick() {
                return goalKick;
            }

            public void setGoalKick(Integer goalKick) {
                this.goalKick = goalKick;
            }

            public Integer getGoalAttempts() {
                return goalAttempts;
            }

            public void setGoalAttempts(Integer goalAttempts) {
                this.goalAttempts = goalAttempts;
            }

            public Integer getFreeKick() {
                return freeKick;
            }

            public void setFreeKick(Integer freeKick) {
                this.freeKick = freeKick;
            }

            public Integer getThrowIn() {
                return throwIn;
            }

            public void setThrowIn(Integer throwIn) {
                this.throwIn = throwIn;
            }

            public Integer getBallSafe() {
                return ballSafe;
            }

            public void setBallSafe(Integer ballSafe) {
                this.ballSafe = ballSafe;
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
