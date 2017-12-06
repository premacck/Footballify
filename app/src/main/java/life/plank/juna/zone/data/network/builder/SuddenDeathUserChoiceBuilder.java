package life.plank.juna.zone.data.network.builder;

import life.plank.juna.zone.data.network.model.FootballMatch;
import life.plank.juna.zone.data.network.model.FootballTeam;
import life.plank.juna.zone.data.network.model.JunaUser;
import life.plank.juna.zone.data.network.model.SuddenDeathUserChoice;

/**
 * Created by plank-sobia on 12/1/2017.
 */

public class SuddenDeathUserChoiceBuilder {
    private SuddenDeathUserChoice suddenDeathUserChoice;
    private static SuddenDeathUserChoiceBuilder suddenDeathUserChoiceBuilder = null;

    private SuddenDeathUserChoiceBuilder() {
        suddenDeathUserChoice = new SuddenDeathUserChoice();
    }

    public static SuddenDeathUserChoiceBuilder getInstance() {
        if (suddenDeathUserChoiceBuilder == null) {
            suddenDeathUserChoiceBuilder = new SuddenDeathUserChoiceBuilder();
        }
        return suddenDeathUserChoiceBuilder;
    }

    public SuddenDeathUserChoiceBuilder withFootballMatch(FootballMatch footballMatch) {
        suddenDeathUserChoice.setFootballMatch(footballMatch);
        return this;
    }

    public SuddenDeathUserChoiceBuilder withJunaUser(JunaUser junaUser) {
        suddenDeathUserChoice.setJunaUser(junaUser);
        return this;
    }

    public SuddenDeathUserChoiceBuilder withTeamSelection(FootballTeam footballTeam) {
        suddenDeathUserChoice.setTeamSelection(footballTeam);
        return this;
    }

    public SuddenDeathUserChoiceBuilder withIsWinner(Boolean isWinner) {
        suddenDeathUserChoice.setIswinner(isWinner);
        return this;
    }

    public SuddenDeathUserChoiceBuilder withLivesRemaining(Integer livesRemaining) {
        suddenDeathUserChoice.setLivesremaining(livesRemaining);
        return this;
    }

    public SuddenDeathUserChoice build() {
        return suddenDeathUserChoice;
    }
}
