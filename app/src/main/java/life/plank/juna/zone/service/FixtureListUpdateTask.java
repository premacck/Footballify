package life.plank.juna.zone.service;

import android.os.AsyncTask;

import java.util.Objects;

import life.plank.juna.zone.data.model.football.FixtureByDate;
import life.plank.juna.zone.data.model.football.FixtureByMatchDay;
import life.plank.juna.zone.data.model.football.LiveScoreData;
import life.plank.juna.zone.data.model.football.LiveTimeStatus;
import life.plank.juna.zone.data.model.football.MatchDetails;
import life.plank.juna.zone.data.model.football.MatchFixture;
import life.plank.juna.zone.ui.football.fragment.LeagueInfoFragment;

import static com.prembros.facilis.util.DataUtilKt.isNullOrEmpty;
import static life.plank.juna.zone.service.MatchDataService.Live.updateScoreLocally;
import static life.plank.juna.zone.service.MatchDataService.Live.updateTimeStatusLocally;

/**
 * Class to update live scores and time status in fixtures' list (in the background thread).
 */
public class FixtureListUpdateTask extends AsyncTask<Void, Void, Void> {

    private final MatchDetails matchDetails;
    private final LiveScoreData scoreData;
    private final LiveTimeStatus timeStatus;
    private boolean isScoreData;

    private FixtureListUpdateTask(MatchDetails matchDetails, LiveScoreData scoreData, LiveTimeStatus timeStatus, boolean isScoreData) {
        this.matchDetails = matchDetails;
        this.scoreData = scoreData;
        this.timeStatus = timeStatus;
        this.isScoreData = isScoreData;
    }

    public static void update(MatchDetails fixture, LiveScoreData scoreData, LiveTimeStatus timeStatus, boolean isScoreData) {
        new FixtureListUpdateTask(fixture, scoreData, timeStatus, isScoreData).execute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MatchFixture matchFixture = findInFixtureList(matchDetails);
        if (matchFixture != null) {
            if (isScoreData) {
                updateScoreLocally(matchFixture, scoreData);
            } else {
                updateTimeStatusLocally(matchFixture, timeStatus);
            }
        }
        return null;
    }

    private MatchFixture findInFixtureList(MatchDetails matchDetails) {
        if (isNullOrEmpty(LeagueInfoFragment.Companion.getFixtureByMatchDayList()) || matchDetails == null)
            return null;

        for (FixtureByMatchDay fixtureByMatchDay : LeagueInfoFragment.Companion.getFixtureByMatchDayList()) {
            for (FixtureByDate fixtureByDate : fixtureByMatchDay.getFixtureByDateList()) {
                for (MatchFixture fixture : fixtureByDate.getFixtures()) {
                    if (Objects.equals(fixture.getMatchId(), matchDetails.getMatchId())) {
                        return fixture;
                    }
                }
            }
        }
        return null;
    }
}