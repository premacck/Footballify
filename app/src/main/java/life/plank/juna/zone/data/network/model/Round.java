package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-sobia on 10/4/2017.
 */

import java.util.List;

public class Round {

    private Integer id;
    private Integer roundNumber;
    private List<FootballMatch> footballMatches = null;
    private List<Object> userChoices = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public List<FootballMatch> getFootballMatches() {
        return footballMatches;
    }

    public void setFootballMatches(List<FootballMatch> footballMatches) {
        this.footballMatches = footballMatches;
    }

    public List<Object> getUserChoices() {
        return userChoices;
    }

    public void setUserChoices(List<Object> userChoices) {
        this.userChoices = userChoices;
    }
}