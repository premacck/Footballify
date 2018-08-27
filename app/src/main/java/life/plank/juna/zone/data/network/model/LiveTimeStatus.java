package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class LiveTimeStatus {
    private String timeStatus;
    private int minute;
    private int extraMinute;

    public LiveTimeStatus(String timeStatus, int minute, int extraMinute) {
        this.timeStatus = timeStatus;
        this.minute = minute;
        this.extraMinute = extraMinute;
    }
}