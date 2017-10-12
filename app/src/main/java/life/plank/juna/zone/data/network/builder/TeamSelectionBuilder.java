package life.plank.juna.zone.data.network.builder;

import life.plank.juna.zone.data.network.model.TeamSelection;

/**
 * Created by plank-sobia on 10/12/2017.
 */

public class TeamSelectionBuilder {

    private TeamSelection teamSelection = null;

    public TeamSelectionBuilder() {
        teamSelection = new TeamSelection();
    }

    public TeamSelectionBuilder withId(Integer id) {
        teamSelection.setId(id);
        return this;
    }

    public TeamSelectionBuilder withName(String name) {
        teamSelection.setName(name);
        return this;
    }

    public TeamSelection build() {
        return teamSelection;
    }
}
