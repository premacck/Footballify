package life.plank.juna.zone.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.data.network.model.SectionedFixture;
import life.plank.juna.zone.util.DateUtil;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.LIVE_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.PAST_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.SCHEDULED_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.TOMORROWS_MATCHES;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DateUtil.getDateFromObject;

public class FootballFixtureClassifierService {

    public enum FixtureSection {
        PAST_MATCHES,
        LIVE_MATCHES,
        TOMORROWS_MATCHES,
        SCHEDULED_MATCHES
    }

    public static List<SectionedFixture> classifyByDate(List<ScoreFixtureModel> fixtures) {
        List<SectionedFixture> sectionedFixtureList = new ArrayList<>();
        Map<String, List<ScoreFixtureModel>> map = new LinkedHashMap<>();
        int i = 0;
        for (int j = 0; j < fixtures.size(); j++) {
            if (i == j) {
                map.put(fixtures.get(i).getMatchDay().toString(), new ArrayList<>());
            }
            if (Objects.equals(fixtures.get(i).getMatchDay(), fixtures.get(j).getMatchDay())) {
                map.get(fixtures.get(i).getMatchDay().toString()).add(fixtures.get(j));
            } else {
                map.put(fixtures.get(j).getMatchDay().toString(), new ArrayList<>());
                i = j;
            }
        }
        for (String matchTime : map.keySet()) {
            List<ScoreFixtureModel> fixtureModels = map.get(matchTime);
            if (!isNullOrEmpty(fixtureModels)) {
                FixtureSection section = getSuitableSection(fixtureModels.get(0).getMatchStartTime());
                sectionedFixtureList.add(SectionedFixture.getFrom(matchTime, section, map.get(matchTime)));
            }
        }
        return sectionedFixtureList;
    }

    private static FixtureSection getSuitableSection(Date matchStartDate) {
        int dateDifference = getDateFromObject(matchStartDate) - getDateFromObject(new Date());
        if (dateDifference >= 2) {
            return SCHEDULED_MATCHES;
        } else if (dateDifference == 1) {
            return TOMORROWS_MATCHES;
        } else if (dateDifference == 0) {
            return LIVE_MATCHES;
        } else {
            return PAST_MATCHES;
        }
    }

    public static List<SectionedFixture> getClassifiedMatchesMap(List<ScoreFixtureModel> fixtures) {
        List<SectionedFixture> sectionedFixtureList = new ArrayList<>();
        List<ScoreFixtureModel> scheduledFixtures = new ArrayList<>();
        List<ScoreFixtureModel> tomorrowsFixtures = new ArrayList<>();
        List<ScoreFixtureModel> liveFixtures = new ArrayList<>();
        List<ScoreFixtureModel> pastFixtures = new ArrayList<>();

        int today = getDateFromObject(new Date());
        for (ScoreFixtureModel fixture : fixtures) {
            int matchStartDate = getDateFromObject(fixture.getMatchStartTime());
            int dateDifference = matchStartDate - today;
            if (dateDifference >= 2) {
                scheduledFixtures.add(fixture);
            } else if (dateDifference == 1) {
                tomorrowsFixtures.add(fixture);
            } else if (dateDifference == 0) {
                liveFixtures.add(fixture);
            } else {
                pastFixtures.add(fixture);
            }
        }

        if (scheduledFixtures.size() > 20) scheduledFixtures = scheduledFixtures.subList(0, 20);
        sectionedFixtureList.add(SectionedFixture.getFrom(PAST_MATCHES, pastFixtures));
        sectionedFixtureList.add(SectionedFixture.getFrom(LIVE_MATCHES, liveFixtures));
        sectionedFixtureList.add(SectionedFixture.getFrom(TOMORROWS_MATCHES, tomorrowsFixtures));
        sectionedFixtureList.add(SectionedFixture.getFrom(SCHEDULED_MATCHES, scheduledFixtures));
        return sectionedFixtureList;
    }

    public static boolean wasMatchYesterday(Date matchStartTime) {
        long timeDifferenceInHours = DateUtil.getDifferenceInHours((matchStartTime), new Date());
        return timeDifferenceInHours < -24 && timeDifferenceInHours > -48;
    }
}