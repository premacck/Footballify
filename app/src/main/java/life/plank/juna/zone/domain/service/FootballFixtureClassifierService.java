package life.plank.juna.zone.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import life.plank.juna.zone.data.network.model.ScoreFixture;
import life.plank.juna.zone.data.network.model.SectionedFixtureDate;
import life.plank.juna.zone.data.network.model.SectionedFixtureMatchDay;
import life.plank.juna.zone.util.DateUtil;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.LIVE_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.PAST_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.SCHEDULED_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.TOMORROWS_MATCHES;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DateUtil.getDateDiffFromToday;
import static life.plank.juna.zone.util.DateUtil.getDateFromObject;
import static life.plank.juna.zone.util.DateUtil.getDateHeader;

public class FootballFixtureClassifierService {

    public static int recyclerViewScrollIndex = 0;

    public enum FixtureSection {
        PAST_MATCHES,
        LIVE_MATCHES,
        TOMORROWS_MATCHES,
        SCHEDULED_MATCHES
    }

    /**
     * Logic behind this method :<br/><br/>
     * 1. We take two pointers to point at indexes of fixtures list entered as param (i and j).<br/><br/>
     * 2. While iterating the fixture list, We keep i fixed and move j ahead.<br/><br/>
     * 3. When i and j are not equal in value, we check if the matchday i equals matchday at j.<br/><br/>
     * If it does, we put a new element in the {@link LinkedHashMap},<br/>
     * which contains the matchday as the key and a classified {@link SectionedFixtureDate} list pertaining to that matchday<br/>
     * and set the "inserted" flag.<br/><br/>
     * If it does not, reset the inserted flag,
     * and move i over to the position of j.<br/>
     */
    public static List<SectionedFixtureMatchDay> classifyByMatchDay(List<ScoreFixture> fixtures) {
        List<SectionedFixtureMatchDay> sectionedFixtureMatchDayList = new ArrayList<>();
//        Map of matchDay and SectionedFixtureDate
        Map<Integer, List<SectionedFixtureDate>> map = new LinkedHashMap<>();
        int i = 0;
        boolean inserted = false;
        for (int j = 0; j < fixtures.size(); j++) {
            int dateDiff = getDateDiffFromToday(fixtures.get(j).getMatchStartTime());
            if (dateDiff <= 0) {
                recyclerViewScrollIndex = j;
            }
            if (i != j) {
                if (Objects.equals(fixtures.get(i).getMatchDay(), fixtures.get(j).getMatchDay()) && !inserted) {
                    map.put(fixtures.get(i).getMatchDay(), classifyByDate(fixtures, fixtures.get(i).getMatchDay()));
                    inserted = true;
                } else {
                    inserted = false;
                    i = j;
                }
            }
        }
        for (Integer matchDay : map.keySet()) {
            List<SectionedFixtureDate> sectionedFixtureDateList = map.get(matchDay);
            if (!isNullOrEmpty(sectionedFixtureDateList)) {
                sectionedFixtureMatchDayList.add(SectionedFixtureMatchDay.getFrom(matchDay.toString(), map.get(matchDay)));
            }
        }
        return sectionedFixtureMatchDayList;
    }

    /**
     * Logic behind this method :<br/><br/>
     * 1. We take two pointers to point at indexes of fixtures list entered as param (i and j).<br/><br/>
     * <p>
     * 2. While iterating the fixture list, We keep i fixed and move j ahead.<br/><br/>
     * <p>
     * 3. When i and j are equal in value, we add a new element to the {@link LinkedHashMap},
     * containing the date (containing "yyyyMMdd" format as int)
     * and an empty {@link ScoreFixture} list which will later contain the fixtures of that date,<br/><br/>
     * <p>
     * 4. If the matchday at i equals matchday at j, we add the fixture at i to the {@link ScoreFixture} list at that date,<br/>
     * else we add a new element to the {@link LinkedHashMap} list,
     * containing the date (containing "yyyyMMdd" format as int)
     * and an empty {@link ScoreFixture} list which will later contain the fixtures of that date,<br/><br/>
     * and add the fixture at j to the list, and then assign i = j.<br/>
     */
    private static List<SectionedFixtureDate> classifyByDate(List<ScoreFixture> fixtures, Integer matchDay) {
        List<SectionedFixtureDate> sectionedFixtureDateList = new ArrayList<>();
//        Map of date and scoreFixtureList
        Map<Integer, List<ScoreFixture>> map = new LinkedHashMap<>();
        int i = 0;
        for (int j = 0; j < fixtures.size(); j++) {
            if (Objects.equals(fixtures.get(j).getMatchDay(), matchDay)) {
                if (i == j) {
                    map.put(getDateFromObject(fixtures.get(i).getMatchStartTime()), new ArrayList<>());
                }
                if (getDateFromObject(fixtures.get(i).getMatchStartTime()) == getDateFromObject(fixtures.get(j).getMatchStartTime())) {
                    map.get(getDateFromObject(fixtures.get(i).getMatchStartTime())).add(fixtures.get(j));
                } else {
                    map.put(getDateFromObject(fixtures.get(j).getMatchStartTime()), new ArrayList<>());
                    map.get(getDateFromObject(fixtures.get(j).getMatchStartTime())).add(fixtures.get(j));
                    i = j;
                }
            }
        }
        for (int date : map.keySet()) {
            List<ScoreFixture> fixtureModels = map.get(date);
            if (!isNullOrEmpty(fixtureModels)) {
                FixtureSection section = getSuitableSection(fixtureModels.get(0).getMatchStartTime());
                sectionedFixtureDateList.add(SectionedFixtureDate.getFrom(
                        getDateHeader(section, fixtureModels.get(0).getMatchStartTime()),
                        section,
                        map.get(date)
                ));
            }
        }
        return sectionedFixtureDateList;
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

    public static boolean wasMatchYesterday(Date matchStartTime) {
        long timeDifferenceInHours = DateUtil.getDifferenceInHours((matchStartTime), new Date());
        return timeDifferenceInHours < -24 && timeDifferenceInHours > -48;
    }
}