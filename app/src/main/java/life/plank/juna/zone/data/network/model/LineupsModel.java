package life.plank.juna.zone.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class LineupsModel {
    private List<List<Formation>> homeTeamFormation = null;
    private List<List<Formation>> awayTeamFormation = null;

    @Data
    public class Formation {
        private String fullName;
        private Integer number;

    }
}
