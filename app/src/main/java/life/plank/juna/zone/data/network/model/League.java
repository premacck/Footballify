package life.plank.juna.zone.data.network.model;

import android.support.annotation.ColorRes;

import lombok.Data;

@Data
public class League {
    private Integer id;
    private Long foreignId;
    private String name;
    private Boolean isCup;
    private String seasonName;
    private String countryName;
    private String thumbUrl;
    private int leagueLogo;

    @ColorRes
    private int dominantColor;

    public League(String name, Boolean isCup, String seasonName, String countryName, String thumbUrl, int dominantColor, int leagueLogo) {
        this.name = name;
        this.isCup = isCup;
        this.seasonName = seasonName;
        this.countryName = countryName;
        this.thumbUrl = thumbUrl;
        this.dominantColor = dominantColor;
        this.leagueLogo = leagueLogo;
    }
}
