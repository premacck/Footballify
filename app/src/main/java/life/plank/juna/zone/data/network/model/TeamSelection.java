package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/10/2017.
 */

public class TeamSelection {

    private static TeamSelection teamSelection = null;
    private Integer id;
    private String name;

    public static TeamSelection getInstance() {
        if (teamSelection == null) {
            teamSelection = new TeamSelection();
        }
        return teamSelection;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TeamSelection withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamSelection withName(String name) {
        this.name = name;
        return this;
    }


}