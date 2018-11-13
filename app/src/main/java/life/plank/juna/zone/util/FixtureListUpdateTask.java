package life.plank.juna.zone.util;

import android.os.AsyncTask;

import java.util.Objects;

import life.plank.juna.zone.data.model.FixtureByDate;
import life.plank.juna.zone.data.model.FixtureByMatchDay;
import life.plank.juna.zone.data.model.LiveScoreData;
import life.plank.juna.zone.data.model.LiveTimeStatus;
import life.plank.juna.zone.data.model.MatchFixture;
import life.plank.juna.zone.view.fragment.football.LeagueInfoFragment;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DataUtil.updateScoreLocally;
import static life.plank.juna.zone.util.DataUtil.updateTimeStatusLocally;

/**
 * Class to update live scores and time status in fixtures' list (in the background thread).
 * TODO : Remove this class after implementation of Android Architecture components.
 */
public class FixtureListUpdateTask extends AsyncTask<Void, Void, Void> {

    private final MatchFixture fixture;
    private final LiveScoreData scoreData;
    private final LiveTimeStatus timeStatus;
    private boolean isScoreData;

    public static void update(MatchFixture fixture, LiveScoreData scoreData, LiveTimeStatus timeStatus, boolean isScoreData) {
        new FixtureListUpdateTask(fixture, scoreData, timeStatus, isScoreData).execute();
    }

    private FixtureListUpdateTask(MatchFixture fixture, LiveScoreData scoreData, LiveTimeStatus timeStatus, boolean isScoreData) {
        this.fixture = fixture;
        this.scoreData = scoreData;
        this.timeStatus = timeStatus;
        this.isScoreData = isScoreData;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MatchFixture matchFixture = findInFixtureList(fixture);
        if (matchFixture != null) {
            if (isScoreData) {
                updateScoreLocally(matchFixture, scoreData);
            } else {
                updateTimeStatusLocally(matchFixture, timeStatus);
            }
        }
        return null;
    }

    private MatchFixture findInFixtureList(MatchFixture matchFixture) {
        if (isNullOrEmpty(LeagueInfoFragment.Companion.getFixtureByMatchDayList()) || matchFixture == null)
            return null;

        for (FixtureByMatchDay fixtureByMatchDay : LeagueInfoFragment.Companion.getFixtureByMatchDayList()) {
            for (FixtureByDate fixtureByDate : fixtureByMatchDay.getFixtureByDateList()) {
                for (MatchFixture fixture : fixtureByDate.getFixtures()) {
                    if (Objects.equals(fixture.getMatchId(), matchFixture.getMatchId())) {
                        return fixture;
                    }
                }
            }
        }
        return null;
    }
}