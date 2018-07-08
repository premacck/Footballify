package life.plank.juna.zone.data.network.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class MatchListModel {
    private int id;
    private int foreignId;
    private int homeGoals;
    private int awayGoals;
    private boolean hasExtraTime;
    private String startDate;

}
