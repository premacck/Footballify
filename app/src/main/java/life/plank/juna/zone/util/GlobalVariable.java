package life.plank.juna.zone.util;

/**
 * Created by plank-arfaa on 26/12/17.
 */

public class GlobalVariable {
    private static GlobalVariable instance;

    private String teamName;

    private GlobalVariable() {
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public static synchronized GlobalVariable getInstance() {
        if (instance == null) {
            instance = new GlobalVariable();
        }
        return instance;
    }
}