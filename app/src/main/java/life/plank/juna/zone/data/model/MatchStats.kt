package life.plank.juna.zone.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class MatchStats @Ignore constructor(
        var homeShots: Int = 0,
        var homeShotsOnTarget: Int = 0,
        var homePossession: Int = 0,
        var homeFouls: Int = 0,
        var homeRedCards: Int = 0,
        var homeYellowCards: Int = 0,
        var homeOffsides: Int = 0,
        var homeCorners: Int = 0,
        var awayShots: Int = 0,
        var awayShotsOnTarget: Int = 0,
        var awayPossession: Int = 0,
        var awayFouls: Int = 0,
        var awayRedCards: Int = 0,
        var awayYellowCards: Int = 0,
        var awayOffsides: Int = 0,
        var awayCorners: Int = 0,
        var errorMessage: Int = 0
) : Parcelable {
    constructor() : this(
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0)
}