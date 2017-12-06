package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 12/1/2017.
 */

public class SuddenDeathUserChoice {
    private JunaUser junaUser;
    private FootballTeam teamSelection;
    private FootballMatch footballMatch;
    private Boolean iswinner;
    private Integer livesremaining;

    public JunaUser getJunaUser() {
        return junaUser;
    }

    public void setJunaUser(JunaUser junaUser) {
        this.junaUser = junaUser;
    }

    public FootballTeam getTeamSelection() {
        return teamSelection;
    }

    public void setTeamSelection(FootballTeam teamSelection) {
        this.teamSelection = teamSelection;
    }

    public FootballMatch getFootballMatch() {
        return footballMatch;
    }

    public void setFootballMatch(FootballMatch footballMatch) {
        this.footballMatch = footballMatch;
    }

    public Boolean getIswinner() {
        return iswinner;
    }

    public void setIswinner(Boolean iswinner) {
        this.iswinner = iswinner;
    }

    public Integer getLivesremaining() {
        return livesremaining;
    }

    public void setLivesremaining(Integer livesremaining) {
        this.livesremaining = livesremaining;
    }
}
