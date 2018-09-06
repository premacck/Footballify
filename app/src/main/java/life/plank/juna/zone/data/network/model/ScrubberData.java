package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class ScrubberData {
    private int range;
    private int xValue;
    private int yValue;
    private String eventType;
    private String description;
    private boolean isHomeTeam;

    public ScrubberData(int xValue, int yValue, String eventType, boolean isHomeTeam) {
        this.xValue = xValue;
        this.yValue = yValue;
        this.eventType = eventType;
        this.isHomeTeam = isHomeTeam;
    }
}