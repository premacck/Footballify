package life.plank.juna.zone.data.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ZoneLiveData(
        var liveEventType: String?,
        var foreignId: Long,
        var boardTopic: String,
        var liveDataType: String,
        var data: String = ""
) : Parcelable {

    fun getScoreData(gson: Gson): LiveScoreData {
        return gson.fromJson<LiveScoreData>(data, LiveScoreData::class.java)
    }

    fun getMatchEventList(gson: Gson): List<MatchEvent>? {
        return gson.fromJson<List<MatchEvent>>(data, object : TypeToken<List<MatchEvent>>() {
        }.type)
    }

    fun getCommentaryList(gson: Gson): List<Commentary>? {
        return gson.fromJson<List<Commentary>>(data, object : TypeToken<List<Commentary>>() {
        }.type)
    }

    fun getLiveTimeStatus(gson: Gson): LiveTimeStatus {
        return gson.fromJson<LiveTimeStatus>(data, LiveTimeStatus::class.java)
    }

    fun getScrubberData(gson: Gson): List<ScrubberData>? {
        return gson.fromJson<List<ScrubberData>>(data, object : TypeToken<List<ScrubberData>>() {
        }.type)
    }

    fun getHighlightsList(gson: Gson): List<Highlights>? {
        return gson.fromJson<List<Highlights>>(data, object : TypeToken<List<Highlights>>() {
        }.type)
    }

    fun getMatchStats(gson: Gson): MatchStats {
        return gson.fromJson<MatchStats>(data, MatchStats::class.java)
    }
}