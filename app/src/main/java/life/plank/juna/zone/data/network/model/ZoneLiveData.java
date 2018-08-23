package life.plank.juna.zone.data.network.model;

import java.util.List;

import lombok.Data;

@Data
public class ZoneLiveData {
    private String liveEventType;
    private long foreignId;
    private String boardTopic;
    private String liveDataType;
    private LiveScoreData scoreData;
    private List<MatchEvent> matchEventList;
    private List<Commentary> commentaryList;
    private LiveTimeStatus liveTimeStatus;
}