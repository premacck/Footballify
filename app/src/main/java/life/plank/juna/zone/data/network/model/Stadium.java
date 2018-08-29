package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class Stadium {
    public Integer capacity;
    public String name;
    public Integer id;
    public Integer foreignId;
    public String surface;
    public String address;
    public String city;
    public String imagePath;
}