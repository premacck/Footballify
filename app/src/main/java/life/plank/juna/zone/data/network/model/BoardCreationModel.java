package life.plank.juna.zone.data.network.model;

public class BoardCreationModel {
    private String id;
    private String displayname;
    private String name;
    private String matchStartTime;
    private String topicName;
    private String boardType;
    private Boolean isActive;
    private String createdBy;
    private String boardMetrics;
    private BoardEvent boardEvent;
    private String interactions;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatchStartTime() {
        return matchStartTime;
    }

    public void setMatchStartTime(String matchStartTime) {
        this.matchStartTime = matchStartTime;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getBoardMetrics() {
        return boardMetrics;
    }

    public void setBoardMetrics(String boardMetrics) {
        this.boardMetrics = boardMetrics;
    }

    public BoardEvent getBoardEvent() {
        return boardEvent;
    }

    public void setBoardEvent(BoardEvent boardEvent) {
        this.boardEvent = boardEvent;
    }

    public String getInteractions() {
        return interactions;
    }

    public void setInteractions(String interactions) {
        this.interactions = interactions;
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
