package life.plank.juna.zone.util;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.FixtureByDate;
import life.plank.juna.zone.data.network.model.FixtureByMatchDay;
import life.plank.juna.zone.data.network.model.LiveScoreData;
import life.plank.juna.zone.data.network.model.LiveTimeStatus;
import life.plank.juna.zone.data.network.model.MatchFixture;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DataUtil.updateScoreLocally;
import static life.plank.juna.zone.util.DataUtil.updateTimeStatusLocally;
import static life.plank.juna.zone.view.activity.LeagueInfoActivity.fixtureByMatchDayList;

/**
 * Class to update live scores and time status in fixtures' list (in the background thread).
 * TODO : Remove this class after implementation of Android Architecture components.
 */
public class FixtureListUpdateTask extends AsyncTask<Void, Void, Void> {

    private final MatchFixture fixture;
    private final LiveScoreData scoreData;
    private final LiveTimeStatus timeStatus;
    private boolean isScoreData;

    /** startTime and endTime to log the runtime of the operation
     * TODO: remove after live testing
     */
    private long startTime;

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
    protected void onPreExecute() {
        super.onPreExecute();
        startTime = System.currentTimeMillis();
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

    @Override
    protected void onPostExecute(Void aVoid) {
//        TODO: Remove this method after live testing
        long endTime = System.currentTimeMillis();
        String timeTaken = (endTime - startTime) + " ms";
        Log.i("FixtureListUpdateTask", "***************** TIME TAKEN ****************: " + timeTaken);
        Toast.makeText(ZoneApplication.getContext(), "FixtureListUpdateTask : Time taken = " + timeTaken, Toast.LENGTH_SHORT).show();
    }

    private MatchFixture findInFixtureList(MatchFixture matchFixture) {
        if (isNullOrEmpty(fixtureByMatchDayList) || matchFixture == null)
            return null;

        for (FixtureByMatchDay fixtureByMatchDay : fixtureByMatchDayList) {
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