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

    public LiveScoreData getLiveScoreDataObject(Gson gson) {
        return gson.fromJson(data, new TypeToken<LiveScoreData>() {
        }.getType());
    }

    public List<LiveScoreData> getLiveScoreDataListObject(Gson gson) {
        return gson.fromJson(data, new TypeToken<List<LiveScoreData>>() {
        }.getType());
    }

    public List<MatchEvent> getMatchEventObject(Gson gson) {
        return gson.fromJson(data, new TypeToken<List<MatchEvent>>() {
        }.getType());
    }
}