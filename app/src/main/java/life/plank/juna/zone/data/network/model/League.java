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
    @ColorRes
    private int dominantColor;

    public League(String name, Boolean isCup, String seasonName, String countryName, String thumbUrl, int dominantColor) {
        this.name = name;
        this.isCup = isCup;
        this.seasonName = seasonName;
        this.countryName = countryName;
        this.thumbUrl = thumbUrl;
        this.dominantColor = dominantColor;
    }
}
