package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class TimeStatus {
    private String timeStatus;
    private int minute;
    private int extraMinute;
}