package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Commentary(
        var matchId: Long = 0,
        var isImportant: Boolean = false,
        var order: Long = 0,
        var isGoal: Boolean = false,
        var minute: Long = 0,
        var extraMinute: Long = 0,
        var comment: String = ""
) : Parcelable