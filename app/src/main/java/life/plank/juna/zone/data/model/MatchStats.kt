package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MatchStats(
        var homeShots: Int?,
        var homeShotsOnTarget: Int?,
        var homePossession: Int?,
        var homeFouls: Int?,
        var homeRedCards: Int?,
        var homeYellowCards: Int?,
        var homeOffsides: Int?,
        var homeCorners: Int?,
        var awayShots: Int?,
        var awayShotsOnTarget: Int?,
        var awayPossession: Int?,
        var awayFouls: Int?,
        var awayRedCards: Int?,
        var awayYellowCards: Int?,
        var awayOffsides: Int?,
        var awayCorners: Int?,
        var errorMessage: Int?
) : Parcelable {
    constructor() : this(
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0)
}