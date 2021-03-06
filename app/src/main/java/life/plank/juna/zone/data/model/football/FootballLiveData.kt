package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.*
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import life.plank.juna.zone.data.model.notification.BaseInAppNotification

@Parcelize
data class FootballLiveData(
        @SerializedName("foreignId") @Expose var matchId: Long,
        var boardTopic: String,
        var liveDataType: String,
        var homeTeamName: String,
        var homeTeamLogo: String,
        @SerializedName("awayTeamName") @Expose var visitingTeamName: String,
        @SerializedName("awayTeamLogo") @Expose var visitingTeamLogo: String,
        var data: String = ""
) : Parcelable, BaseInAppNotification() {

    fun getScoreData(gson: Gson): LiveScoreData? {
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

    fun getLiveTimeStatus(gson: Gson): LiveTimeStatus? {
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

    fun getMatchStats(gson: Gson): MatchStats? {
        return gson.fromJson<MatchStats>(data, MatchStats::class.java)
    }
}