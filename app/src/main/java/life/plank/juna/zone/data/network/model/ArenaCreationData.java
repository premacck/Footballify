package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/4/2017.
 */

public class ArenaCreationData {

    public static ArenaCreationData arenaData = null;
    private Creator creator;
    private String gameType;

    public static ArenaCreationData getInstance() {
        if (arenaData == null) {
            arenaData = new ArenaCreationData();
        }
        return arenaData;
    }

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

    public ArenaCreationData withCreator(Creator creator) {
        this.creator = creator;
        return this;
    }

    public ArenaCreationData withGameType(String gameType) {
        this.gameType = gameType;
        return this;
    }
}