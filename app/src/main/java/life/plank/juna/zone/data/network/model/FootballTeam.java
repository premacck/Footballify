package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class FootballTeam {
    private Integer id;
    private Integer foreignId;
    private String name;
    private Boolean isNationalTeam;
    private Integer founded;
    private String logoLink;
}
