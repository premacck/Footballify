package life.plank.juna.zone.data.network.model;

public class BoardCreationModel {
    private String id;
    private String displayname;
    private Object name;
    private String matchStartTime;
    private Object topicName;
    private String boardType;
    private Boolean isActive;
    private Object createdBy;
    private Object boardMetrics;
    private BoardEvent boardEvent;
    private Object interactions;

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

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public String getMatchStartTime() {
        return matchStartTime;
    }

    public void setMatchStartTime(String matchStartTime) {
        this.matchStartTime = matchStartTime;
    }

    public Object getTopicName() {
        return topicName;
    }

    public void setTopicName(Object topicName) {
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

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getBoardMetrics() {
        return boardMetrics;
    }

    public void setBoardMetrics(Object boardMetrics) {
        this.boardMetrics = boardMetrics;
    }

    public BoardEvent getBoardEvent() {
        return boardEvent;
    }

    public void setBoardEvent(BoardEvent boardEvent) {
        this.boardEvent = boardEvent;
    }

    public Object getInteractions() {
        return interactions;
    }

    public void setInteractions(Object interactions) {
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
