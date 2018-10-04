package life.plank.juna.zone.data.network.model;


import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Board {
    private String id;
    private String displayname;
    private String name;
    private String boardType;
    private boolean isActive;
    private BoardEvent boardEvent;
    private String zone;
    private String description;
    private String color;
    private User owner;
    private String boardIconUrl;
    private Interaction interactions;
    private Date startDate;
    private Date endDate;

    @Data
    public static class BoardEvent {
        private String type;
        private Integer foreignId;

    }
}
