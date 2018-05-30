package life.plank.juna.zone.domain.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import life.plank.juna.zone.BaseUnitTest;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FootballFixtureClassifierServiceTest extends BaseUnitTest {
    Date pastDateFormat,liveDateFormat,tomorrowsDateFormat,futureDateFormat;
    @InjectMocks
    private FootballFixtureClassifierService fixtureClassifierService;

    private List<ScoreFixtureModel> scoreFixtureModels;
    DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    @Before
    public void setUp() {
        super.init();
        Calendar c = Calendar.getInstance();
        c.setTime( new Date() );
        c.add(Calendar.DATE, -5);
        Date pastDate = c.getTime();
        c.setTime(new Date());
        c.add(Calendar.HOUR, -1);
        Date liveDate = c.getTime();
        c.setTime( new Date());
        c.add( Calendar.HOUR, 15 );
        Date tomorrowsDate = c.getTime();
        c.setTime( new Date() );
        c.add( Calendar.HOUR, 60 );
        Date futureDate =  c.getTime();

        scoreFixtureModels = Stream.of(
                new ScoreFixtureModel()
                {{
                    setMatchStartTime(pastDate);
                    setId( 1 );
                    setMatchDay( 1 );
                }},
                new ScoreFixtureModel()
                {{
                    setMatchStartTime(liveDate);
                    setId (2);
                    setMatchDay( 2 );
                }},
                new ScoreFixtureModel()
                {{
                    setMatchStartTime( liveDate );
                    setId(3);
                    setMatchDay(2);
                }},
                new ScoreFixtureModel()
                {{
                    setMatchStartTime( tomorrowsDate );
                    setId(4);
                    setMatchDay(3);
                }},
                new ScoreFixtureModel()
                {{
                    setMatchStartTime( futureDate );
                    setId(5);
                    setMatchDay(3);
                }},new ScoreFixtureModel()
                {{
                    setMatchStartTime( futureDate );
                    setId(6);
                    setMatchDay(3);
                }}
        ).collect( Collectors.toList());
    }

    @Test
    public void testThatFixturesAreClassifiedAccordingToMatchDay(){
        HashMap<Integer, List<ScoreFixtureModel>> fixtureMap = fixtureClassifierService.GetMatchDayMap(scoreFixtureModels);
        assertThat(fixtureMap.get(1).size(), is(1));
        assertThat(fixtureMap.get(2).size(), is( 2));
        assertThat(fixtureMap.get(3).size(), is( 3));
    }

    @Test
    public void testThatFixturesAreClassifiedAccordingToCategory(){
        HashMap<FootballFixtureClassifierService.FixtureClassification, List<ScoreFixtureModel>> classifiedMatchesMap = fixtureClassifierService.GetClassifiedMatchesMap( scoreFixtureModels );
        assertThat(classifiedMatchesMap.get( FootballFixtureClassifierService.FixtureClassification.PAST_MATCHES).size(), is(1));
        assertThat(classifiedMatchesMap.get(FootballFixtureClassifierService.FixtureClassification.LIVE_MATCHES).size(), is( 2));
        assertThat(classifiedMatchesMap.get(FootballFixtureClassifierService.FixtureClassification.TOMORROWS_MATCHES).size(), is( 1));
        assertThat(classifiedMatchesMap.get(FootballFixtureClassifierService.FixtureClassification.SCHEDULED_MATCHES).size(), is( 2));
    }
}