package life.plank.juna.zone.data.network.model;

import java.util.List;

import lombok.Data;

@Data
public class Lineups {
    private List<List<Formation>> homeTeamFormation;
    private List<List<Formation>> awayTeamFormation;
    private final String homeTeamName;
    private final String visitingTeamName;
    private final String homeManagerName;
    private final String visitingManagerName;

    @Data
    public class Formation {
        private String fullName;
        private int number;
        private int formationNumber;
        private int yellowCard;
        private int redCard;
        private int yellowRed;
        private int goals;
        private int substituteIn;
        private int substituteOut;
    }
}
