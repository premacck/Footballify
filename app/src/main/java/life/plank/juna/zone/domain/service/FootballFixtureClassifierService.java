package life.plank.juna.zone.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.util.DateUtil;

public class FootballFixtureClassifierService {

    public enum FixtureClassification{
        PAST_MATCHES,
        LIVE_MATCHES,
        TOMORROWS_MATCHES,
        SCHEDULED_MATCHES
    }

    public HashMap<Integer, List<ScoreFixtureModel>> GetMatchDayMap(List<ScoreFixtureModel> fixtures) {
        HashMap<Integer, List<ScoreFixtureModel>> matchDayMap = new HashMap<>();
        for (ScoreFixtureModel match: fixtures) {
            if (matchDayMap.get(match.getMatchDay()) == null)
                matchDayMap.put(match.getMatchDay(), new ArrayList<>() );
            matchDayMap.get(match.getMatchDay()).add(match);
        }
        return matchDayMap;
    }

    public HashMap<FixtureClassification, List<ScoreFixtureModel>> GetClassifiedMatchesMap(List<ScoreFixtureModel> fixtures){
        HashMap<FixtureClassification, List<ScoreFixtureModel>> classifiedMatchesMap = new HashMap<>();
        classifiedMatchesMap.put(FixtureClassification.PAST_MATCHES, new ArrayList<>(  ));
        classifiedMatchesMap.put( FixtureClassification.LIVE_MATCHES, new ArrayList<>(  ) );
        classifiedMatchesMap.put( FixtureClassification.TOMORROWS_MATCHES, new ArrayList<>(  ) );
        classifiedMatchesMap.put( FixtureClassification.SCHEDULED_MATCHES, new ArrayList<>(  ) );

        Date currentTime = new Date();

        // todo: Fix the tomorrow's scheduled matches appropriately
        for(ScoreFixtureModel fixture: fixtures) {

                long timeDifferenceInHours = DateUtil.getDifferenceInHours((fixture.getMatchStartTime()), currentTime);
                if (timeDifferenceInHours > 48) {
                    classifiedMatchesMap.get( FixtureClassification.SCHEDULED_MATCHES ).add( fixture );
                } else if (timeDifferenceInHours > 0) {
                    classifiedMatchesMap.get( FixtureClassification.TOMORROWS_MATCHES ).add( fixture );
                } else if (timeDifferenceInHours <= 0
                        && timeDifferenceInHours >= -3) {
                    classifiedMatchesMap.get( FixtureClassification.LIVE_MATCHES ).add( fixture );
                } else {
                    classifiedMatchesMap.get( FixtureClassification.PAST_MATCHES ).add( fixture );
                }
            }
        return classifiedMatchesMap;
    }
}
