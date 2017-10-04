package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/4/2017.
 */

import java.util.List;

public class Arena {

    private Integer id;
    private String dateCreated;
    private Integer leagueYearStart;
    private Integer status;
    private String invitationCode;
    private String gameType;
    private FootballLeague footballLeague;
    private List<Round> rounds = null;
    private List<Player> players = null;
    private Creator creator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getLeagueYearStart() {
        return leagueYearStart;
    }

    public void setLeagueYearStart(Integer leagueYearStart) {
        this.leagueYearStart = leagueYearStart;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public FootballLeague getFootballLeague() {
        return footballLeague;
    }

    public void setFootballLeague(FootballLeague footballLeague) {
        this.footballLeague = footballLeague;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }
}