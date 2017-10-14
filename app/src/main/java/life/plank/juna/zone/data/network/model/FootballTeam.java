package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/10/2017.
 */

public class FootballTeam {

    private Integer id;
    private String name;
    private FootballLeague footballLeague;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FootballLeague getFootballLeague() {
        return footballLeague;
    }

    public void setFootballLeague(FootballLeague footballLeague) {
        this.footballLeague = footballLeague;
    }
}