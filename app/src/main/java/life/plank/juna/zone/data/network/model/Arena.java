package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/4/2017.
 */

import java.util.List;

public class Arena {

    private static Arena arena = null;
    private Integer id;
    private String dateCreated;
    private Integer leagueYearStart;
    private Integer status;
    private String invitationCode;
    private String gameType;
    private FootballLeague footballLeague;
    private List<Round> rounds = null;
    private List<Player> players = null;
    private JunaUser creator;

    public static Arena getInstance() {
        if (arena == null)
            arena = new Arena();
        return arena;
    }

    public void copyArena(Arena arena) {
        setId(arena.getId());
        setDateCreated(arena.getDateCreated());
        setLeagueYearStart(arena.getLeagueYearStart());
        setStatus(arena.getStatus());
        setInvitationCode(arena.getInvitationCode());
        setGameType(arena.getGameType());
        setFootballLeague(arena.getFootballLeague());
        setRounds(arena.getRounds());
        setPlayers(arena.getPlayers());
        setCreator(arena.getCreator());
    }

    public static Arena getNullArena() {
        arena = new Arena();
        return arena;
    }

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

    public JunaUser getCreator() {
        return creator;
    }

    public void setCreator(JunaUser creator) {
        this.creator = creator;
    }
}