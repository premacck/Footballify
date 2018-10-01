package life.plank.juna.zone.data.network.model;

import java.util.Set;

import lombok.Data;

@Data
public class Zones {

    public String name;
    public String category;
    public String id;
    public Set<String> zoneIds;

}