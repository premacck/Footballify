package life.plank.juna.zone.data.network.model;


import lombok.Data;

@Data
public class Board {
    private String id;
    private String displayname;
    private String matchStartTime;
    private String boardType;
    private Boolean isActive;
    private BoardEvent boardEvent;
    private String zone;
    private String description;
    private String color;

    @Data
    public class BoardEvent {
        private String type;
        private Integer foreignId;

    }
}
