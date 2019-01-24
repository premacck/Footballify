package life.plank.juna.zone.service;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.football.FootballLiveData;
import life.plank.juna.zone.data.model.football.Formation;
import life.plank.juna.zone.data.model.football.FormationList;
import life.plank.juna.zone.data.model.football.Lineups;
import life.plank.juna.zone.data.model.football.LiveScoreData;
import life.plank.juna.zone.data.model.football.LiveTimeStatus;
import life.plank.juna.zone.data.model.football.MatchDetails;
import life.plank.juna.zone.data.model.football.MatchEvent;
import life.plank.juna.zone.data.model.football.MatchFixture;
import life.plank.juna.zone.util.football.ScoreBuilder;

import static life.plank.juna.zone.util.common.AppConstants.DASH;
import static life.plank.juna.zone.util.common.AppConstants.FT;
import static life.plank.juna.zone.util.common.AppConstants.FULL_TIME_LOWERCASE;
import static life.plank.juna.zone.util.common.AppConstants.GOAL;
import static life.plank.juna.zone.util.common.AppConstants.HALF_TIME_LOWERCASE;
import static life.plank.juna.zone.util.common.AppConstants.HT;
import static life.plank.juna.zone.util.common.AppConstants.NOT_STARTED;
import static life.plank.juna.zone.util.common.AppConstants.NS;
import static life.plank.juna.zone.util.common.AppConstants.RED_CARD;
import static life.plank.juna.zone.util.common.AppConstants.SUBSTITUTION;
import static life.plank.juna.zone.util.common.AppConstants.WIDE_DASH;
import static life.plank.juna.zone.util.common.AppConstants.YELLOW_CARD;
import static life.plank.juna.zone.util.common.AppConstants.YELLOW_RED;
import static life.plank.juna.zone.util.time.DateUtil.getDateDiffFromToday;
import static life.plank.juna.zone.util.time.DateUtil.getFutureMatchTime;
import static life.plank.juna.zone.util.time.DateUtil.getTimeDiffFromNow;

/**
 * Class intended for match related services
 */
public class MatchDataService {

    public static String getSeparator(MatchDetails matchDetails, ImageView winPointer, boolean isBoard) {
        winPointer.setVisibility(View.INVISIBLE);
        int dateDiff = getDateDiffFromToday(matchDetails.getMatchStartTime());
        switch (dateDiff) {
            case -1:
                return getPastMatchSeparator(matchDetails, winPointer, isBoard);
            case 0:
                if (getTimeDiffFromNow(matchDetails.getMatchStartTime()) <= 0) {
                    return matchDetails.getHomeGoals() + (isBoard ? DASH : WIDE_DASH) + matchDetails.getAwayGoals();
                } else {
                    return getFutureMatchTime(matchDetails.getMatchStartTime());
                }
            case 1:
                return getFutureMatchTime(matchDetails.getMatchStartTime());
            default:
                if (dateDiff < -1) {
                    return getPastMatchSeparator(matchDetails, winPointer, isBoard);
                } else {
                    return getFutureMatchTime(matchDetails.getMatchStartTime());
                }
        }
    }

    private static String getPastMatchSeparator(MatchDetails matchDetails, ImageView winPointer, boolean isBoard) {
        String teamNameSeparator;
        int homeWinDrawable = isBoard ?
                R.drawable.ic_win_home_light :
                R.drawable.ic_win_home_dark;
        int visitingWinDrawable = isBoard ?
                R.drawable.ic_win_away_light :
                R.drawable.ic_win_away_dark;
        if (matchDetails.getAwayTeamPenaltyScore() == 0 && matchDetails.getHomeTeamPenaltyScore() == 0) {
            if (matchDetails.getHomeGoals() > matchDetails.getAwayGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(homeWinDrawable);
                teamNameSeparator = ScoreBuilder.getWinScore(matchDetails.getHomeGoals(), matchDetails.getAwayGoals(), isBoard);
            } else if (matchDetails.getAwayGoals() > matchDetails.getHomeGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(visitingWinDrawable);
                teamNameSeparator = ScoreBuilder.getWinScore(matchDetails.getHomeGoals(), matchDetails.getAwayGoals(), isBoard);
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                teamNameSeparator = ScoreBuilder.getTiedScore(matchDetails.getHomeGoals(), matchDetails.getAwayGoals(), isBoard);
            }
        } else {
            if (matchDetails.getHomeGoals() > matchDetails.getAwayGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(homeWinDrawable);
                teamNameSeparator = ScoreBuilder.getWinPenaltyScore(matchDetails.getHomeGoals(), matchDetails.getHomeTeamPenaltyScore(),
                        matchDetails.getAwayGoals(), matchDetails.getAwayTeamPenaltyScore(), isBoard);
            } else if (matchDetails.getAwayGoals() > matchDetails.getHomeGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(visitingWinDrawable);
                teamNameSeparator = ScoreBuilder.getWinPenaltyScore(matchDetails.getHomeGoals(), matchDetails.getHomeTeamPenaltyScore(),
                        matchDetails.getAwayGoals(), matchDetails.getAwayTeamPenaltyScore(), isBoard);
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                teamNameSeparator = ScoreBuilder.getTiedPenaltyScore(matchDetails.getHomeGoals(), matchDetails.getHomeTeamPenaltyScore(),
                        matchDetails.getAwayGoals(), matchDetails.getAwayTeamPenaltyScore(), isBoard);
            }
        }
        return teamNameSeparator;
    }

    public static String getFormattedExtraMinutes(int minute) {
        return minute > 9 ? minute + ".00" : "0" + minute + ".00";
    }

    public static List<MatchEvent> extractSubstitutionEvents(List<MatchEvent> matchEvents) {
        List<MatchEvent> newMatchEventList = new ArrayList<>(matchEvents);
        for (MatchEvent matchEvent : matchEvents) {
            if (!Objects.equals(matchEvent.getEventType(), SUBSTITUTION)) {
                newMatchEventList.remove(matchEvent);
            }
        }
        return newMatchEventList;
    }

    public static String getDisplayTimeStatus(String apiTimeStatus) {
        switch (apiTimeStatus) {
            case HT:
                return HALF_TIME_LOWERCASE;
            case FT:
                return FULL_TIME_LOWERCASE;
            case NS:
                return NOT_STARTED;
            default:
                return apiTimeStatus;
        }
    }

    public static Lineups getIntegratedLineups(Lineups lineups, List<MatchEvent> matchEvents) {
        for (MatchEvent matchEvent : matchEvents) {
            if (matchEvent.isHomeTeam()) {
                integrateLineupsWithMatchEvents(lineups.getHomeTeamFormation(), matchEvent);
            } else {
                integrateLineupsWithMatchEvents(lineups.getAwayTeamFormation(), matchEvent);
            }
        }
        return lineups;
    }

    private static void integrateLineupsWithMatchEvents(List<FormationList> allFormationList, MatchEvent matchEvent) {
        for (List<Formation> formationList : allFormationList) {
            for (Formation formation : formationList) {
                if (Objects.equals(formation.getNickname(), matchEvent.getPlayerName())) {
                    switch (matchEvent.getEventType()) {
                        case GOAL:
                            formation.setGoals(formation.getGoals() + 1);
                            break;
                        case YELLOW_CARD:
                            formation.setYellowCards(1);
                            break;
                        case RED_CARD:
                            formation.setRedCards(1);
                            break;
                        case YELLOW_RED:
                            formation.setYellowRed(1);
                            break;
                        case SUBSTITUTION:
                            formation.setSubstituteOut(1);
                            break;
                    }
                }
            }
        }
    }

    public static class Live {

        public static FootballLiveData getZoneLiveData(Intent intent, String key, Gson gson) {
            return gson.fromJson(intent.getStringExtra(key), new TypeToken<FootballLiveData>() {
            }.getType());
        }

        public static void updateScoreLocally(MatchFixture fixture, LiveScoreData scoreData) {
            fixture.setHomeGoals(scoreData.getHomeGoals());
            fixture.setAwayGoals(scoreData.getAwayGoals());
            fixture.setHomeTeamPenaltyScore(scoreData.getHomeTeamPenaltyScore());
            fixture.setAwayTeamPenaltyScore(scoreData.getAwayTeamPenaltyScore());
        }

        public static void updateScoreLocally(MatchDetails matchDetails, LiveScoreData scoreData) {
            matchDetails.setHomeGoals(scoreData.getHomeGoals());
            matchDetails.setAwayGoals(scoreData.getAwayGoals());
            matchDetails.setHomeTeamPenaltyScore(scoreData.getHomeTeamPenaltyScore());
            matchDetails.setAwayTeamPenaltyScore(scoreData.getAwayTeamPenaltyScore());
        }

        public static void updateTimeStatusLocally(MatchFixture fixture, LiveTimeStatus timeStatus) {
            fixture.setTimeStatus(timeStatus.getTimeStatus());
            fixture.setMinute(timeStatus.getMinute());
            fixture.setExtraMinute(timeStatus.getExtraMinute());
        }

        public static void updateTimeStatusLocally(MatchDetails matchDetails, LiveTimeStatus timeStatus) {
            matchDetails.setTimeStatus(timeStatus.getTimeStatus());
            matchDetails.setMinute(timeStatus.getMinute());
            matchDetails.setExtraMinute(timeStatus.getExtraMinute());
        }
    }
}
