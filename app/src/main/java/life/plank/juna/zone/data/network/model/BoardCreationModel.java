package life.plank.juna.zone.data.network.model;

public class BoardCreationModel {
    private String id;
    private String displayname;
    private String matchStartTime;
    private String boardType;
    private Boolean isActive;
    private BoardEvent boardEvent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getMatchStartTime() {
        return matchStartTime;
    }

    public void setMatchStartTime(String matchStartTime) {
        this.matchStartTime = matchStartTime;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public BoardEvent getBoardEvent() {
        return boardEvent;
    }

    public void setBoardEvent(BoardEvent boardEvent) {
        this.boardEvent = boardEvent;
    }

    public class BoardEvent {
        private String type;
        private Integer foreignId;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getForeignId() {
            return foreignId;
        }

        public void setForeignId(Integer foreignId) {
            this.foreignId = foreignId;
        }
    }
}
