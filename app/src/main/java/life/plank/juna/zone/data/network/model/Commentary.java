package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class Commentary {
    private long matchId;
    private boolean isImportant;
    private long order;
    private boolean isGoal;
    private long minute;
    private long extraMinute;
    private String comment;
}