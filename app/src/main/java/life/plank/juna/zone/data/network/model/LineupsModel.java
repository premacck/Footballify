package life.plank.juna.zone.data.network.model;

import java.util.List;

import lombok.Data;

@Data
public class LineupsModel {
    private List<List<Formation>> homeTeamFormation = null;
    private List<List<Formation>> awayTeamFormation = null;

    @Data
    public class Formation {
        private String fullName;
        private Integer number;
        private Integer formationNumber;
    }
}
