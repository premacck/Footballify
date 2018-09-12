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
    private User owner;

    public Board(String displayname, String boardType, String zone, String description, String color) {
        this.displayname = displayname;
        this.boardType = boardType;
        this.zone = zone;
        this.description = description;
        this.color = color;
    }

    @Data
    public class BoardEvent {
        private String type;
        private Integer foreignId;

    }
}
