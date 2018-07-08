package life.plank.juna.zone.data.network.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class PlayerStatsModel {

    private Integer id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String nickname;
    private String dateOfBirth;
    private String position;
    private Integer foreignId;
    private Integer number;
    private Boolean isInjured;
    private Integer minutes;
    private Integer appearances;
    private Integer lineups;
    private Integer substituteIn;
    private Integer substituteOut;
    private Integer substitutesOnBench;
    private Integer goals;
    private Integer assists;
    private Integer yellowcards;
    private Integer yellowred;
    private Integer redcards;
    private Integer squadRef;
    private Integer countryRef;
    private String nationality;
    private String birthcountry;
    private String birthplace;
    private String height;
    private String weight;
    private String imagePath;
    private Integer trackingState;
    private String entityIdentifier;

}