package life.plank.juna.zone.data.network.model;

import lombok.Data;

/**
 * Created by plank-sobia on 10/3/2017.
 */

@Data
public class JunaUser {

    private Integer id;
    private String username;
    private String password;
    private String displayName;
    private String provider;

}