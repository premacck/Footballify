package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class ScrubberData {
    private int millisecondsX;
    private int interactionY;
    private MatchEvent event;

    public ScrubberData(int millisecondsX, int interactionY, String eventType, boolean isHomeTeam) {
        this.millisecondsX = millisecondsX;
        this.interactionY = interactionY;
        this.event = new MatchEvent(eventType, isHomeTeam);
    }
}