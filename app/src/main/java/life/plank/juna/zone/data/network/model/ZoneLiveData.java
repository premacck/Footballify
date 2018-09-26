package life.plank.juna.zone.data.network.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import lombok.Data;

@Data
public class ZoneLiveData {
    private String liveEventType;
    private long foreignId;
    private String boardTopic;
    private String liveDataType;
    private String data;

    public LiveScoreData getScoreData(Gson gson) {
        return gson.fromJson(data, LiveScoreData.class);
    }

    public List<MatchEvent> getMatchEventList(Gson gson) {
        return gson.fromJson(data, new TypeToken<List<MatchEvent>>() {
        }.getType());
    }

    public List<Commentary> getCommentaryList(Gson gson) {
        return gson.fromJson(data, new TypeToken<List<Commentary>>() {
        }.getType());
    }

    public LiveTimeStatus getLiveTimeStatus(Gson gson) {
        return gson.fromJson(data, LiveTimeStatus.class);
    }

    public List<ScrubberData> getScrubberData(Gson gson) {
        return gson.fromJson(data, new TypeToken<List<ScrubberData>>() {
        }.getType());
    }

    public List<Highlights> getHighlightsList(Gson gson) {
        return gson.fromJson(data, new TypeToken<List<Highlights>>() {
        }.getType());
    }

    public MatchStats getMatchStats(Gson gson) {
        return gson.fromJson(data, MatchStats.class);
    }
}