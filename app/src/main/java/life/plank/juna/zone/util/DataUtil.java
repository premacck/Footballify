package life.plank.juna.zone.util;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.data.network.model.SectionedFixture;
import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.LIVE_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.PAST_MATCHES;
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
        return "" + (i > 9 ? i : "0" + i);
    }

    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return (c == null) || c.isEmpty();
    }

    public static boolean equalsNullString(String s) {
        return Objects.equals(s, "null");
    }

    public static String getSeparator(ScoreFixtureModel scoreFixture, FixtureSection fixtureSection, ImageView winPointer) {
        if (fixtureSection == PAST_MATCHES) {
            return getPastMatchSeparator(scoreFixture, winPointer);
        } else if (fixtureSection == LIVE_MATCHES) {
            if (getTimeDiffFromNow(scoreFixture.getMatchStartTime()) < 0) {
                winPointer.setVisibility(View.INVISIBLE);
                return "" + scoreFixture.getHomeGoals() + "  -  " + scoreFixture.getAwayGoals();
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                return getFutureMatchTime(scoreFixture.getMatchStartTime());
            }
        } else {
            winPointer.setVisibility(View.INVISIBLE);
            return getFutureMatchTime(scoreFixture.getMatchStartTime());
        }
    }

    private static String getPastMatchSeparator(ScoreFixtureModel scoreFixture, ImageView winPointer) {
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
    public static List<SectionedFixture> removeEmptySections(List<SectionedFixture> list) {
        List<SectionedFixture> newList = new ArrayList<>(list);
        for (SectionedFixture fixture : newList) {
            if (fixture.getScoreFixtureModelList().isEmpty()) {
                list.remove(fixture);
            }
        }
        newList.clear();
        return list;
    }
}