package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Formation(
        var nickname: String,
        var imagePath: String,
        var number: Int,
        var yellowCards: Int,
        var redCards: Int,
        var yellowRed: Int,
        var goals: Int,
        var substituteIn: Int,
        var substituteOut: Int
) : Parcelable
