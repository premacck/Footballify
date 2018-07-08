package life.plank.juna.zone.data.network.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by plank-sobia on 10/4/2017.
 */

@Data
public class Player {

    private Integer id;
    private String username;
    private String provider;
    private String displayName;

}