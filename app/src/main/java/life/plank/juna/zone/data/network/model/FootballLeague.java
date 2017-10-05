package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/4/2017.
 */

import java.util.List;

public class FootballLeague {

    private Integer id;
    private String name;
    private List<Object> footballTeams = null;

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

    public List<Object> getFootballTeams() {
        return footballTeams;
    }

    public void setFootballTeams(List<Object> footballTeams) {
        this.footballTeams = footballTeams;
    }
}