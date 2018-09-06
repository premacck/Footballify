package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class League {
    private Integer id;
    private Long foreignId;
    private String name;
    private Boolean isCup;
    private Integer countryRef;
}
