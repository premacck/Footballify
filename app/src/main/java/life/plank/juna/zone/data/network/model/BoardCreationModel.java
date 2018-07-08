package life.plank.juna.zone.data.network.model;


import lombok.Data;

@Data
public class BoardCreationModel {
    private String id;
    private String displayname;
    private String matchStartTime;
    private String boardType;
    private Boolean isActive;
    private BoardEvent boardEvent;

    @Data
    public class BoardEvent {
        private String type;
        private Integer foreignId;

    }
}
