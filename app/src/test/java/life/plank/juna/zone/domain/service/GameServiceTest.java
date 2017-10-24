package life.plank.juna.zone.domain.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.data.network.model.HomeTeam;
import life.plank.juna.zone.data.network.model.VisitingTeam;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by plank-sobia on 10/23/2017.
 */
public class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    private FootballMatch footballMatch;
    private HomeTeam homeTeam;
    private VisitingTeam visitingTeam;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        footballMatch = new FootballMatch();
        homeTeam = new HomeTeam();
        visitingTeam = new VisitingTeam();
    }

    @Test
    public void computeWinnerWhenHomeTeamAndVisitingTeamScoreIsEqual() {
        homeTeam.setName("Man United");
        visitingTeam.setName("Southampton");
        footballMatch.setHomeTeamScore(1);
        footballMatch.setVisitingTeamScore(1);
        footballMatch.setHomeTeam(homeTeam);
        footballMatch.setVisitingTeam(visitingTeam);
        assertThat(this.gameService.computeWinner(footballMatch, "Southampton"), is(true));
        assertThat(gameService.computeWinner(footballMatch, "Man United"), is(true));
    }

    @Test
    public void computeWinnerWhenUserGuessesCorrectTeamName() {
        homeTeam.setName("Man United");
        visitingTeam.setName("Chelsea");
        footballMatch.setHomeTeamScore(0);
        footballMatch.setVisitingTeamScore(1);
        footballMatch.setHomeTeam(homeTeam);
        footballMatch.setVisitingTeam(visitingTeam);
        assertThat(gameService.computeWinner(footballMatch, "Chelsea"), is(true));
    }

    @Test
    public void computeWinnerWhenUserGuessesWrongTeamName() {
        homeTeam.setName("Arsenal");
        visitingTeam.setName("Chelsea");
        footballMatch.setHomeTeamScore(2);
        footballMatch.setVisitingTeamScore(1);
        footballMatch.setHomeTeam(homeTeam);
        footballMatch.setVisitingTeam(visitingTeam);
        assertThat(gameService.computeWinner(footballMatch, "Chelsea"), is(false));
    }

    @Test
    public void computeScoreWhenVisitingTeamIsWinnerAndUserChoosesVisitingTeam() {
        homeTeam.setName("Liverpool");
        visitingTeam.setName("Leicester");
        footballMatch.setHomeTeamScore(1);
        footballMatch.setVisitingTeamScore(2);
        footballMatch.setHomeTeam(homeTeam);
        footballMatch.setVisitingTeam(visitingTeam);
        assertThat(gameService.computeScore(footballMatch, "Leicester", 0, 2, 0), is(2));
    }

    @Test
    public void computeScoreWhenVisitingTeamIsWinnerAndUserChoosesHomeTeam() {
        homeTeam.setName("Arsenal");
        visitingTeam.setName("Chelsea");
        footballMatch.setHomeTeamScore(1);
        footballMatch.setVisitingTeamScore(2);
        footballMatch.setHomeTeam(homeTeam);
        footballMatch.setVisitingTeam(visitingTeam);
        assertThat(gameService.computeScore(footballMatch, "Arsenal", 2, 1, 2), is(1));
    }

    @Test
    public void computeScoreWhenHomeTeamIsWinnerAndUserChoosesHomeTeam() {
        homeTeam.setName("Aston Villa");
        visitingTeam.setName("Blackburn");
        footballMatch.setHomeTeamScore(3);
        footballMatch.setVisitingTeamScore(1);
        footballMatch.setHomeTeam(homeTeam);
        footballMatch.setVisitingTeam(visitingTeam);
        assertThat(gameService.computeScore(footballMatch, "Aston Villa", 2, 1, -2), is(-1));
    }

    @Test
    public void computeScoreWhenHomeTeamIsWinnerAndUserChoosesVisitingTeam() {
        homeTeam.setName("Arsenal");
        visitingTeam.setName("Coventry");
        footballMatch.setHomeTeamScore(2);
        footballMatch.setVisitingTeamScore(0);
        footballMatch.setHomeTeam(homeTeam);
        footballMatch.setVisitingTeam(visitingTeam);
        assertThat(gameService.computeScore(footballMatch, "Coventry", 1, 2, 0), is(-2));
    }

    @Test
    public void computeScoreForDrawMatch() {
        homeTeam.setName("Everton");
        visitingTeam.setName("Crystal Palace");
        footballMatch.setHomeTeamScore(1);
        footballMatch.setVisitingTeamScore(1);
        footballMatch.setHomeTeam(homeTeam);
        footballMatch.setVisitingTeam(visitingTeam);
        assertThat(gameService.computeScore(footballMatch, "Crystal Palace", 1, 2, 0), is(0));
    }
}