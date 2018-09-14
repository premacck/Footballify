package life.plank.juna.zone.data.network.model;

import java.util.List;

import lombok.Data;

@Data
public class Lineups {
    private List<List<Formation>> homeTeamFormation;
    private List<List<Formation>> awayTeamFormation;
    private String homeTeamName;
    private String visitingTeamName;
    private String homeManagerName;
    private String visitingManagerName;
    private int errorMessage;

    @Data
    public class Formation {
        private String nickname;
        private String imagePath;
        private int number;
        private int formationNumber;
        //        TODO : change yellowcard, redcard and yellowred fields to camelCase when they are updated in backend
        private int yellowcard;
        private int redcard;
        private int yellowred;
        private int goals;
        private int substituteIn;
        private int substituteOut;
    }
}
