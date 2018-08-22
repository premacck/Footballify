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
    private Board board;

    @Data
    public class BoardEvent {
        private String type;
        private Integer foreignId;

    }

    private static Board boardInstance;

    private Board() {
    }  //private constructor.

    public static Board getInstance() {
        if (boardInstance == null) { //if there is no instance available. create new one.
            boardInstance = new Board();
        }

        return boardInstance;
    }
}
