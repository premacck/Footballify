package life.plank.juna.zone.data.diffutil;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import life.plank.juna.zone.data.model.football.MatchFixture;
import life.plank.juna.zone.data.model.football.Stadium;

public class MatchFixtureDiffCallback extends DiffUtil.Callback{

    private List<MatchFixture> oldMatchFixtureList;
    private List<MatchFixture> newMatchFixtureList;
    private Gson gson;

    public MatchFixtureDiffCallback(List<MatchFixture> oldMatchFixtureList, List<MatchFixture> newMatchFixtureList, Gson gson) {
        this.oldMatchFixtureList = oldMatchFixtureList;
        this.newMatchFixtureList = newMatchFixtureList;
        this.gson = gson;
    }

    @Override
    public int getOldListSize() {
        return oldMatchFixtureList.size();
    }

    @Override
    public int getNewListSize() {
        return newMatchFixtureList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldMatchFixtureList.get(oldItemPosition).getMatchId(), newMatchFixtureList.get(newItemPosition).getMatchId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldMatchFixtureList.get(oldItemPosition), newMatchFixtureList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        MatchFixture oldMatchFixture = oldMatchFixtureList.get(oldItemPosition);
        MatchFixture newMatchFixture = newMatchFixtureList.get(newItemPosition);

        if (oldMatchFixture == null || newMatchFixture == null) {
            return null;
        }
        Bundle diffBundle = new Bundle();
        try {
            if (!Objects.equals(oldMatchFixture.getMatchId(), newMatchFixture.getMatchId())) {
                diffBundle.putString(DiffConstants.DIFF_MATCH_FIXTURE, gson.toJson(newMatchFixture));
            } else return getDiffBundle(oldMatchFixture, newMatchFixture, diffBundle);
        } catch (Exception e) {
            Log.e("MatchFixtureDiff", "getChangePayload: ", e);
        }
        return null;
    }

    private Bundle getDiffBundle(MatchFixture oldMatchFixture, MatchFixture newMatchFixture, Bundle diffBundle) {
        if (!Objects.equals(oldMatchFixture.getHomeGoals(), newMatchFixture.getHomeGoals())) {
            diffBundle.putInt(DiffConstants.DIFF_HOME_GOALS, newMatchFixture.getHomeGoals());
        }
        if (!Objects.equals(oldMatchFixture.getAwayGoals(), newMatchFixture.getAwayGoals())) {
            diffBundle.putInt(DiffConstants.DIFF_AWAY_GOALS, newMatchFixture.getAwayGoals());
        }
        if (!Objects.equals(oldMatchFixture.getHomeTeamPenaltyScore(), newMatchFixture.getHomeTeamPenaltyScore())) {
            diffBundle.putInt(DiffConstants.DIFF_HOME_PENALTY_SCORE, newMatchFixture.getHomeTeamPenaltyScore());
        }
        if (!Objects.equals(oldMatchFixture.getAwayTeamPenaltyScore(), newMatchFixture.getAwayTeamPenaltyScore())) {
            diffBundle.putInt(DiffConstants.DIFF_AWAY_PENALTY_SCORE, newMatchFixture.getAwayTeamPenaltyScore());
        }
        if (!Objects.equals(oldMatchFixture.getTimeStatus(), newMatchFixture.getTimeStatus())) {
            diffBundle.putString(DiffConstants.DIFF_TIME_STATUS, newMatchFixture.getTimeStatus());
        }
        if (!Objects.equals(oldMatchFixture.getMinute(), newMatchFixture.getMinute())) {
            diffBundle.putInt(DiffConstants.DIFF_MINUTE, newMatchFixture.getMinute());
        }
        if (!Objects.equals(oldMatchFixture.getExtraMinute(), newMatchFixture.getExtraMinute())) {
            diffBundle.putInt(DiffConstants.DIFF_EXTRA_MINUTE, newMatchFixture.getExtraMinute());
        }
        if (!Objects.equals(oldMatchFixture.getMatchStartTime(), newMatchFixture.getMatchStartTime())) {
            diffBundle.putSerializable(DiffConstants.DIFF_MATCH_START_TIME, newMatchFixture.getMatchStartTime());
        }
        if (!Objects.equals(oldMatchFixture.getVenue(), newMatchFixture.getVenue())) {
            diffBundle.putString(DiffConstants.DIFF_VENUE, gson.toJson(newMatchFixture.getVenue()));
        }
        return diffBundle;
    }

    public static void updateMatchFixtureByDiffBundle(MatchFixture oldMatchFixture, Bundle diffBundle, Gson gson) {
        for (String key : diffBundle.keySet()) {
            try {
                switch (key) {
                    case DiffConstants.DIFF_HOME_GOALS:
                        oldMatchFixture.setHomeGoals(diffBundle.getInt(DiffConstants.DIFF_HOME_GOALS));
                        break;
                    case DiffConstants.DIFF_AWAY_GOALS:
                        oldMatchFixture.setAwayGoals(diffBundle.getInt(DiffConstants.DIFF_AWAY_GOALS));
                        break;
                    case DiffConstants.DIFF_HOME_PENALTY_SCORE:
                        oldMatchFixture.setHomeTeamPenaltyScore(diffBundle.getInt(DiffConstants.DIFF_HOME_PENALTY_SCORE));
                        break;
                    case DiffConstants.DIFF_AWAY_PENALTY_SCORE:
                        oldMatchFixture.setAwayTeamPenaltyScore(diffBundle.getInt(DiffConstants.DIFF_AWAY_PENALTY_SCORE));
                        break;
                    case DiffConstants.DIFF_TIME_STATUS:
                        oldMatchFixture.setTimeStatus(diffBundle.getString(DiffConstants.DIFF_TIME_STATUS));
                        break;
                    case DiffConstants.DIFF_MINUTE:
                        oldMatchFixture.setMinute(diffBundle.getInt(DiffConstants.DIFF_MINUTE));
                        break;
                    case DiffConstants.DIFF_EXTRA_MINUTE:
                        oldMatchFixture.setExtraMinute(diffBundle.getInt(DiffConstants.DIFF_EXTRA_MINUTE));
                        break;
                    case DiffConstants.DIFF_MATCH_START_TIME:
                        oldMatchFixture.setMatchStartTime((Date) diffBundle.getSerializable(DiffConstants.DIFF_MATCH_START_TIME));
                        break;
                    case DiffConstants.DIFF_VENUE:
                        oldMatchFixture.setVenue(gson.fromJson(diffBundle.getString(DiffConstants.DIFF_VENUE), Stadium.class));
                        break;
                    default:
                        break;
                }
            } catch (JsonSyntaxException e) {
                Log.e("MatchFixtureDiff", "updateMatchFixtureByDiffBundle: ", e);
            }
        }
    }
}