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
    private List<ScrubberData> scrubberDataList;

    public ZoneLiveData(String liveEventType, long foreignId, String boardTopic, String liveDataType,
                        LiveScoreData scoreData, List<MatchEvent> matchEventList, List<Commentary> commentaryList,
                        List<ScrubberData> scrubberDataList, LiveTimeStatus liveTimeStatus) {
        this.liveEventType = liveEventType;
        this.foreignId = foreignId;
        this.boardTopic = boardTopic;
        this.liveDataType = liveDataType;
        this.scoreData = scoreData;
        this.matchEventList = matchEventList;
        this.commentaryList = commentaryList;
        this.scrubberDataList = scrubberDataList;
        this.liveTimeStatus = liveTimeStatus;
    }
}