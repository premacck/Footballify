package life.plank.juna.zone.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Poll(
        var pollId: String,
        var canAnswer: Boolean,
        @SerializedName("homeTeamPercentile") @Expose var homeTeamPercent: Int,
        @SerializedName("awayTeamPercentile") @Expose var awayTeamPercent: Int,
        @SerializedName("drawPercentile") @Expose var drawPercent: Int,
        var totalVotes: Long,
        var userAnswer: String
) : Parcelable