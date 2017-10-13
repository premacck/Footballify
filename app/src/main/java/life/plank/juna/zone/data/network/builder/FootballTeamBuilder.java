package life.plank.juna.zone.data.network.builder;

import life.plank.juna.zone.data.network.model.FootballLeague;
import life.plank.juna.zone.data.network.model.FootballTeam;

/**
 * Created by plank-sobia on 10/12/2017.
 */

public class FootballTeamBuilder {

    private FootballTeam footballTeam = null;

    public FootballTeamBuilder() {
        footballTeam = new FootballTeam();
    }

    public FootballTeamBuilder withId(Integer id) {
        footballTeam.setId(id);
        return this;
    }

    public FootballTeamBuilder withName(String name) {
        footballTeam.setName(name);
        return this;
    }

    public FootballTeamBuilder withFootballLeague(FootballLeague footballLeague) {
        footballTeam.setFootballLeague(footballLeague);
        return this;
    }

    public FootballTeam build() {
        return footballTeam;
    }
}
