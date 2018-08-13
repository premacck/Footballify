package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class FootballTeam {

    private Integer id;
    private Long foreignId;
    private String name;
    private Integer countryRef;
    private Boolean isNationalTeam;
    private Integer founded;
    private Integer homeStadiumRef;
    private String logoLink;
}
