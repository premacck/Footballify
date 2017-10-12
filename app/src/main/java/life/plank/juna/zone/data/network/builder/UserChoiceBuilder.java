package life.plank.juna.zone.data.network.builder;

import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.data.network.model.JunaUser;
import life.plank.juna.zone.data.network.model.TeamSelection;
import life.plank.juna.zone.data.network.model.UserChoice;

/**
 * Created by plank-sobia on 10/12/2017.
 */

public class UserChoiceBuilder {

    private UserChoice userChoice = null;

    public UserChoiceBuilder() {
        userChoice = new UserChoice();
    }

    public UserChoiceBuilder withId(Integer id) {
        userChoice.setId(id);
        return this;
    }

    public UserChoiceBuilder withHomeTeamScore(Integer homeTeamScore) {
        userChoice.setHomeTeamScore(homeTeamScore);
        return this;
    }

    public UserChoiceBuilder withVisitingTeamScore(Integer visitingTeamScore) {
        userChoice.setVisitingTeamScore(visitingTeamScore);
        return this;
    }

    public UserChoiceBuilder withPoints(Integer points) {
        userChoice.setPoints(points);
        return this;
    }

    public UserChoiceBuilder withJunaUser(JunaUser junaUser) {
        userChoice.setJunaUser(junaUser);
        return this;
    }

    public UserChoiceBuilder withTeamSelection(TeamSelection teamSelection) {
        userChoice.setTeamSelection(teamSelection);
        return this;
    }

    public UserChoiceBuilder withFootballMatch(FootballMatch footballMatch) {
        userChoice.setFootballMatch(footballMatch);
        return this;
    }

    public UserChoice build() {
        return userChoice;
    }
}
