package life.plank.juna.zone.util.common;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.data.model.Formation;
import life.plank.juna.zone.data.model.FormationList;
import life.plank.juna.zone.data.model.League;
import life.plank.juna.zone.data.model.Lineups;
import life.plank.juna.zone.data.model.LiveScoreData;
import life.plank.juna.zone.data.model.LiveTimeStatus;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.MatchEvent;
import life.plank.juna.zone.data.model.MatchFixture;
import life.plank.juna.zone.data.model.ZoneLiveData;
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

public class DataUtil {

    public static String findString(@StringRes int stringRes) {
        return ZoneApplication.getContext().getString(stringRes);
    }

    public static CharSequence findString(@StringRes int stringRes, Object... formatArgs) {
        return ZoneApplication.getContext().getString(stringRes, formatArgs);
    }

    public static Integer findInt(@IntegerRes int integerRes) {
        return ZoneApplication.getContext().getResources().getInteger(integerRes);
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNullOrEmpty(Object s) {
        return s == null;
    }

    public static boolean isNullOrEmpty(CharSequence s) {
        return s == null;
    }

    public static String formatInt(int i) {
        return String.valueOf(i > 9 ? i : "0" + i);
    }

    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return (c == null) || c.isEmpty();
    }

    public static boolean equalsNullString(String s) {
        return Objects.equals(s, "null");
    }

    public static boolean isValidEmail(CharSequence target) {
        return (target != null && !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

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

    public static ZoneLiveData getZoneLiveData(Intent intent, String key, Gson gson) {
        return gson.fromJson(intent.getStringExtra(key), new TypeToken<ZoneLiveData>() {
        }.getType());
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

    public static List<League> getStaticLeagues() {
        List<League> footballFeeds = new ArrayList<>();
        footballFeeds.add(new League(
                8,
                "Premier League",
                false,
                "2018/2019",
                "England",
                "https://image.ibb.co/msPsep/img_epl_logo.png",
                R.color.black_currant,
                R.drawable.img_epl_logo
        ));
        footballFeeds.add(new League(
                564,
                "La Liga",
                false,
                "2018/2019",
                "Spain",
                "https://cdn.bleacherreport.net/images/team_logos/328x328/la_liga.png",
                R.color.eclipse,
                R.drawable.img_laliga_logo
        ));
        footballFeeds.add(new League(
                82,
                "Bundesliga",
                false,
                "2018/2019",
                "Germany",
                "http://logok.org/wp-content/uploads/2014/12/Bundesliga-logo-880x655.png",
                R.color.sangria,
                R.drawable.img_bundesliga_logo
        ));
        footballFeeds.add(new League(
                2,
                "Champions League",
                false,
                "2018/2019",
                "Europe",
                "https://www.seeklogo.net/wp-content/uploads/2013/06/uefa-champions-league-eps-vector-logo.png",
                R.color.maire,
                R.drawable.img_champions_league_logo
        ));
        footballFeeds.add(new League(
                384,
                "Serie A",
                false,
                "2018/2019",
                "Italy",
                "http://www.tvsette.net/wp-content/uploads/2017/06/SERIE-A-LOGO.png",
                R.color.crusoe,
                R.drawable.img_serie_a_logo
        ));
        footballFeeds.add(new League(
                301,
                "Ligue 1",
                false,
                "2018/2019",
                "France",
                "http://logok.org/wp-content/uploads/2014/11/Ligue-1-logo-france-880x660.png",
                R.color.shuttle_grey,
                R.drawable.img_ligue_1_logo
        ));
        footballFeeds.add(new League(
                24,
                "FA Cup",
                true,
                "2018/2019",
                "England",
                "https://vignette.wikia.nocookie.net/logopedia/images/3/33/The_Emirates_FA_Cup.png",
                R.color.sapphire,
                R.drawable.img_fa_cup_logo
        ));
        footballFeeds.add(new League(
                570,
                "Copa Del Rey",
                true,
                "2018/2019",
                "Spain",
                "https://www.primeradivision.pl/luba/dane/pliki/bank_zdj/duzy/copadelrey.jpg",
                R.color.carmine,
                R.drawable.img_delrey_logo
        ));
        footballFeeds.add(new League(
                390,
                "Coppa Italia",
                true,
                "2018/2019",
                "Italy",
                "https://cdn.ghanasoccernet.com/2018/07/5b3f92288827c.jpg",
                R.color.husk,
                R.drawable.img_coppa_italia_logo
        ));
        footballFeeds.add(new League(
                5,
                "Europa League",
                false,
                "2018/2019",
                "Europe",
                "https://cdn.foxsports.com.br/sites/foxsports-br/files/img/competition/shields-original/logo-uefa-europa-league.png",
                R.color.carrot_orange,
                R.drawable.img_europa_logo
        ));
        return footballFeeds;
    }

    public static League getSpecifiedLeague(String leagueName) {
        if (isNullOrEmpty(leagueName)) return null;

        List<League> leagues = getStaticLeagues();
        for (League league : leagues) {
            if (Objects.equals(league.getName(), leagueName) || league.getName().equalsIgnoreCase(leagueName)) {
                return league;
            }
        }
        return null;
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

    public static void pinFeedEntry(List<FeedEntry> feedEntryList, FeedEntry feedEntryToPin) {
        int index = feedEntryList.indexOf(feedEntryToPin);
        if (index >= 0) {
            feedEntryList.remove(feedEntryToPin);
            feedEntryList.add(0, feedEntryToPin);
        }
    }

    public static void unpinFeedEntry(List<FeedEntry> feedEntryList, FeedEntry feedEntryToUnpin) {
        int previousPosition = feedEntryToUnpin.getFeedInteractions().getPreviousPosition();
        if (previousPosition >= 0) {
            feedEntryList.remove(feedEntryToUnpin);
            feedEntryList.add(previousPosition, feedEntryToUnpin);
        }
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

    public static <T> void validateAndUpdateList(List<T> originalList, List<T> newList, boolean isError) {
        if (!isError) {
            if (originalList == null) {
                originalList = new ArrayList<>();
            }
            if (!isNullOrEmpty(newList)) {
                originalList.addAll(newList);
            }
        }
    }
}