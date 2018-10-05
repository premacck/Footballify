package life.plank.juna.zone.data.model

import android.os.Parcelable
import android.support.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class League(
        var name: String = "",
        var isCup: Boolean,
        var seasonName: String?,
        var countryName: String?,
        var thumbUrl: String?,
        var dominantColor: Int?,
        @DrawableRes var leagueLogo: Int,
        var foreignId: Long = 0,
        var id: Int = 0
) : Parcelable {
    constructor(name: String, isCup: Boolean, seasonName: String?, countryName: String?, thumbUrl: String?, dominantColor: Int?, leagueLogo: Int) :
            this(name, isCup, seasonName, countryName, thumbUrl, dominantColor, leagueLogo, 0, 0)
}