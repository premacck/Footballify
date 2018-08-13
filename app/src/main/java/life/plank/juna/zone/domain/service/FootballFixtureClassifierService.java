package life.plank.juna.zone.domain.service;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.data.network.model.SectionedFixture;
import life.plank.juna.zone.util.DateUtil;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.LIVE_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.PAST_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.SCHEDULED_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.TOMORROWS_MATCHES;

public class FootballFixtureClassifierService {

    public enum FixtureSection {
        PAST_MATCHES,
        LIVE_MATCHES,
        TOMORROWS_MATCHES,
        SCHEDULED_MATCHES
    }

    /**
     * TODO : remove this method if not in use
     */
    public SparseArray<List<ScoreFixtureModel>> getMatchDayMap(List<ScoreFixtureModel> fixtures) {
        SparseArray<List<ScoreFixtureModel>> matchDayMap = new SparseArray<>();
        for (ScoreFixtureModel match : fixtures) {
            if (matchDayMap.get(match.getMatchDay()) == null)
                matchDayMap.put(match.getMatchDay(), new ArrayList<>());
            matchDayMap.get(match.getMatchDay()).add(match);
        }
        return matchDayMap;
    }

    /**
     * TODO : remove this method if not in use
     */
    public HashMap<FixtureSection, List<ScoreFixtureModel>> GetClassifiedMatchesMap(List<ScoreFixtureModel> fixtures) {
        HashMap<FixtureSection, List<ScoreFixtureModel>> classifiedMatchesMap = new HashMap<>();
        classifiedMatchesMap.put(PAST_MATCHES, new ArrayList<>());
        classifiedMatchesMap.put(LIVE_MATCHES, new ArrayList<>());
        classifiedMatchesMap.put(TOMORROWS_MATCHES, new ArrayList<>());
        classifiedMatchesMap.put(SCHEDULED_MATCHES, new ArrayList<>());

        Date currentTime = new Date();

        for (ScoreFixtureModel fixture : fixtures) {
            long timeDifferenceInHours = DateUtil.getDifferenceInHours((fixture.getMatchStartTime()), currentTime);
            if (timeDifferenceInHours > 48) {
                classifiedMatchesMap.get(SCHEDULED_MATCHES).add(fixture);
            } else if (timeDifferenceInHours > 3 && timeDifferenceInHours < 48) {
                classifiedMatchesMap.get(TOMORROWS_MATCHES).add(fixture);
            } else if (timeDifferenceInHours >= -3 && timeDifferenceInHours < 0) {
                classifiedMatchesMap.get(LIVE_MATCHES).add(fixture);
            } else {
                classifiedMatchesMap.get(PAST_MATCHES).add(fixture);
            }
        }
        return classifiedMatchesMap;
    }

    public static List<SectionedFixture> getClassifiedMatchesMap(List<ScoreFixtureModel> fixtures) {
        List<SectionedFixture> sectionedFixtureList = new ArrayList<>();
        List<ScoreFixtureModel> scheduledFixtures = new ArrayList<>();
        List<ScoreFixtureModel> tomorrowsFixtures = new ArrayList<>();
        List<ScoreFixtureModel> liveFixtures = new ArrayList<>();
        List<ScoreFixtureModel> pastFixtures = new ArrayList<>();

        Date currentTime = new Date();

        for (ScoreFixtureModel fixture : fixtures) {
            long timeDifferenceInHours = DateUtil.getDifferenceInHours((fixture.getMatchStartTime()), currentTime);
            if (timeDifferenceInHours > 48) {
                scheduledFixtures.add(fixture);
            } else if (timeDifferenceInHours >= 24 && timeDifferenceInHours < 48) {
                tomorrowsFixtures.add(fixture);
            } else if (timeDifferenceInHours >= -3 && timeDifferenceInHours < 24) {
                liveFixtures.add(fixture);
            } else {
                pastFixtures.add(fixture);
            }
        }
        if (scheduledFixtures.size() > 10) scheduledFixtures = scheduledFixtures.subList(0, 20);
        sectionedFixtureList.add(SectionedFixture.getFrom(PAST_MATCHES, pastFixtures));
        sectionedFixtureList.add(SectionedFixture.getFrom(LIVE_MATCHES, liveFixtures));
        sectionedFixtureList.add(SectionedFixture.getFrom(TOMORROWS_MATCHES, tomorrowsFixtures));
        sectionedFixtureList.add(SectionedFixture.getFrom(SCHEDULED_MATCHES, scheduledFixtures));
        return sectionedFixtureList;
    }
}