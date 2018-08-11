package life.plank.juna.zone.domain.service;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.util.DateUtil;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureClassification.LIVE_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureClassification.PAST_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureClassification.SCHEDULED_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureClassification.TOMORROWS_MATCHES;

public class FootballFixtureClassifierService {

    public enum FixtureClassification {
        PAST_MATCHES,
        LIVE_MATCHES,
        TOMORROWS_MATCHES,
        SCHEDULED_MATCHES
    }

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
     * TODO : Please document the working of this method
     */
    public HashMap<FixtureClassification, List<ScoreFixtureModel>> GetClassifiedMatchesMap(List<ScoreFixtureModel> fixtures) {
        HashMap<FixtureClassification, List<ScoreFixtureModel>> classifiedMatchesMap = new HashMap<>();
        classifiedMatchesMap.put(PAST_MATCHES, new ArrayList<>());
        classifiedMatchesMap.put(LIVE_MATCHES, new ArrayList<>());
        classifiedMatchesMap.put(TOMORROWS_MATCHES, new ArrayList<>());
        classifiedMatchesMap.put(SCHEDULED_MATCHES, new ArrayList<>());

        Date currentTime = new Date();

        // todo: Fix the tomorrow's scheduled matches appropriately
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

//    public List<SectionedFixture> GetClassifiedMatchesMap(List<ScoreFixtureModel> fixtures) {
//        List<SectionedFixture> sectionedFixtureList = new ArrayList<>();
//
//        Date currentTime = new Date();
//
//        // to do: Fix the tomorrow's scheduled matches appropriately
//        for (ScoreFixtureModel fixture : fixtures) {
//            long timeDifferenceInHours = DateUtil.getDifferenceInHours((fixture.getMatchStartTime()), currentTime);
//            if (timeDifferenceInHours > 48) {
//                sectionedFixtureList.add(new SectionedFixture(SCHEDULED_MATCHES, fixture))
//            } else if (timeDifferenceInHours > 3 && timeDifferenceInHours < 48) {
//                classifiedMatchesMap.get(TOMORROWS_MATCHES).add(fixture);
//            } else if (timeDifferenceInHours >= -3 && timeDifferenceInHours < 0) {
//                classifiedMatchesMap.get(LIVE_MATCHES).add(fixture);
//            } else {
//                classifiedMatchesMap.get(PAST_MATCHES).add(fixture);
//            }
//        }
//        return classifiedMatchesMap;
//    }
}