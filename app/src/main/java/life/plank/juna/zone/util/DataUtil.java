package life.plank.juna.zone.util;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.ScoreFixture;
import life.plank.juna.zone.data.network.model.SectionedFixtureDate;
import life.plank.juna.zone.data.network.model.SectionedFixtureMatchDay;
import life.plank.juna.zone.data.network.model.ZoneLiveData;
import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.LIVE_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.PAST_MATCHES;
import static life.plank.juna.zone.util.AppConstants.SUBSTITUTION;
import static life.plank.juna.zone.util.DateUtil.getFutureMatchTime;
import static life.plank.juna.zone.util.DateUtil.getTimeDiffFromNow;

public class DataUtil {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNullOrEmpty(CharSequence s) {
        return s == null;
    }

    static String formatInt(int i) {
        return String.valueOf(i > 9 ? i : "0" + i);
    }

    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return (c == null) || c.isEmpty();
    }

    public static boolean equalsNullString(String s) {
        return Objects.equals(s, "null");
    }

    public static String getSeparator(ScoreFixture scoreFixture, FixtureSection fixtureSection, ImageView winPointer) {
        if (fixtureSection == PAST_MATCHES) {
            return getPastMatchSeparator(scoreFixture, winPointer);
        } else if (fixtureSection == LIVE_MATCHES) {
            if (getTimeDiffFromNow(scoreFixture.getMatchStartTime()) < 0) {
                winPointer.setVisibility(View.INVISIBLE);
                return scoreFixture.getHomeGoals() + "  -  " + scoreFixture.getAwayGoals();
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                return getFutureMatchTime(scoreFixture.getMatchStartTime());
            }
        } else {
            winPointer.setVisibility(View.INVISIBLE);
            return getFutureMatchTime(scoreFixture.getMatchStartTime());
        }
    }

    private static String getPastMatchSeparator(ScoreFixture scoreFixture, ImageView winPointer) {
        String teamNameSeparator;
        if (scoreFixture.getAwayTeamPenaltyScore() == 0 && scoreFixture.getHomeTeamPenaltyScore() == 0) {
            if (scoreFixture.getHomeGoals() > scoreFixture.getAwayGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(R.drawable.ic_win_home);
                teamNameSeparator = scoreFixture.getHomeGoals() + "     " +
                        scoreFixture.getAwayGoals();
            } else if (scoreFixture.getAwayGoals() > scoreFixture.getHomeGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(R.drawable.ic_win_away);
                teamNameSeparator = scoreFixture.getHomeGoals() + "     " +
                        scoreFixture.getAwayGoals();
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                teamNameSeparator = scoreFixture.getHomeGoals() + "  -  " +
                        scoreFixture.getAwayGoals();
            }
        } else {
            if (scoreFixture.getHomeGoals() > scoreFixture.getAwayGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(R.drawable.ic_win_home);
                teamNameSeparator = scoreFixture.getHomeGoals() +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" + "     " +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" +
                        scoreFixture.getAwayGoals();
            } else if (scoreFixture.getAwayGoals() > scoreFixture.getHomeGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(R.drawable.ic_win_away);
                teamNameSeparator = scoreFixture.getHomeGoals() +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" + "     " +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" +
                        scoreFixture.getAwayGoals();
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                teamNameSeparator = scoreFixture.getHomeGoals() +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" + "  -  " +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" +
                        scoreFixture.getAwayGoals();
            }
        }
        return teamNameSeparator;
    }

    /**
     * Removes empty sections in the sectionsList, if any.
     */
    public static List<SectionedFixtureMatchDay> removeEmptyMatchDays(List<SectionedFixtureMatchDay> list) {
        List<SectionedFixtureMatchDay> newList = new ArrayList<>(list);
        for (SectionedFixtureMatchDay fixture : newList) {
            if (fixture.getSectionedFixtureDateList().isEmpty()) {
                list.remove(fixture);
            }
        }
        newList.clear();
        return list;
    }

    public static List<SectionedFixtureDate> removeEmptyFixtureDates(List<SectionedFixtureDate> list) {
        List<SectionedFixtureDate> newList = new ArrayList<>(list);
        for (SectionedFixtureDate fixture : newList) {
            if (fixture.getScoreFixtureList().isEmpty()) {
                list.remove(fixture);
            }
        }
        newList.clear();
        return list;
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
}