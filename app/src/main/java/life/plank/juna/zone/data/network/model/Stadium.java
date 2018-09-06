package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class Stadium {
    private Integer capacity;
    private String name;
    private Integer id;
    private Integer foreignId;
    private String surface;
    private String address;
    private String city;
    private String imagePath;
}