package life.plank.juna.zone.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.data.network.model.SectionedFixture;
import life.plank.juna.zone.util.DateUtil;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.LIVE_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.PAST_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.SCHEDULED_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.TOMORROWS_MATCHES;
import static life.plank.juna.zone.util.DateUtil.getDateFromObject;

public class FootballFixtureClassifierService {

    public enum FixtureSection {
        PAST_MATCHES,
        LIVE_MATCHES,
        TOMORROWS_MATCHES,
        SCHEDULED_MATCHES
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

        Collections.reverse(scheduledFixtures);
        Collections.reverse(pastFixtures);
        Collections.reverse(liveFixtures);
        Collections.reverse(tomorrowsFixtures);

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