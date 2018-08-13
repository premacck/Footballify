package life.plank.juna.zone.util;

import android.content.Context;

import java.util.Collection;
import java.util.Objects;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.PAST_MATCHES;

public class DataUtil {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return (c == null) || c.isEmpty();
    }

    public static boolean equalsNullString(String s) {
        return Objects.equals(s, "null");
    }

    public static String getSeparator(Context context, ScoreFixtureModel scoreFixture, FixtureSection fixtureSection) {
        return fixtureSection == PAST_MATCHES ? getPastMatchSeparator(context, scoreFixture) : " - ";
    }

    private static String getPastMatchSeparator(Context context, ScoreFixtureModel scoreFixture) {
        String teamNameSeparator;
        if (scoreFixture.getAwayTeamPenaltyScore() == 0 && scoreFixture.getHomeTeamPenaltyScore() == 0) {
            if (scoreFixture.getHomeTeamScore() > scoreFixture.getAwayTeamScore()) {
                teamNameSeparator = scoreFixture.getHomeTeamScore() + " " +
                        context.getString(R.string.home_team_won) + " " +
                        scoreFixture.getAwayTeamScore();
            } else if (scoreFixture.getAwayTeamScore() > scoreFixture.getHomeTeamScore()){
                teamNameSeparator = scoreFixture.getHomeTeamScore() + " " +
                        context.getString(R.string.away_team_won) + " " +
                        scoreFixture.getAwayTeamScore();
            } else {
                teamNameSeparator = scoreFixture.getHomeTeamScore() +
                        " - " +
                        scoreFixture.getAwayTeamScore();
            }
        } else {
            if (scoreFixture.getHomeTeamScore() > scoreFixture.getAwayTeamScore()) {
                teamNameSeparator = scoreFixture.getHomeTeamScore() +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" + " " +
                        context.getString(R.string.home_team_won) + " " +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" +
                        scoreFixture.getAwayTeamScore();
            } else if (scoreFixture.getAwayTeamScore() > scoreFixture.getHomeTeamScore()){
                teamNameSeparator = scoreFixture.getHomeTeamScore() +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" + " " +
                        context.getString(R.string.away_team_won) + " " +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" +
                        scoreFixture.getAwayTeamScore();
            } else {
                teamNameSeparator = scoreFixture.getHomeTeamScore() +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" + " " +
                        " - " +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" +
                        scoreFixture.getAwayTeamScore();
            }
        }
        return teamNameSeparator;
    }
}