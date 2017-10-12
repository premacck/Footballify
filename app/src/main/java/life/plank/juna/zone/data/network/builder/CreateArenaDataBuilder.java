package life.plank.juna.zone.data.network.builder;

import life.plank.juna.zone.data.network.model.CreateArenaData;
import life.plank.juna.zone.data.network.model.Creator;

/**
 * Created by plank-sobia on 10/12/2017.
 */

public class CreateArenaDataBuilder {
    private CreateArenaData createArenaData = null;

    public CreateArenaDataBuilder() {
        createArenaData = new CreateArenaData();
    }

    public CreateArenaDataBuilder withCreator(Creator creator) {
        createArenaData.setCreator(creator);
        return this;
    }

    public CreateArenaDataBuilder withGameType(String gameType) {
        createArenaData.setGameType(gameType);
        return this;
    }

    public CreateArenaData build() {
        return createArenaData;
    }
}
