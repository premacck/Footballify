package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/4/2017.
 */

public class CreateArenaData {

    private Creator creator;
    private String gameType;

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }
}