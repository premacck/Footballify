package life.plank.juna.zone.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.os.Parcelable
import android.support.annotation.DrawableRes
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class League @Ignore constructor(
        var name: String = "",
        var isCup: Boolean,
        var seasonName: String?,
        var countryName: String?,
        var thumbUrl: String?,
        var dominantColor: Int?,
        @DrawableRes var leagueLogo: Int,
        @SerializedName("foreignId") @Expose var id: Int = 0
) : Parcelable {
    @Ignore constructor(name: String, isCup: Boolean, seasonName: String?, countryName: String?, thumbUrl: String?, dominantColor: Int?, leagueLogo: Int) :
            this(name, isCup, seasonName, countryName, thumbUrl, dominantColor, leagueLogo, 0)

    constructor() : this("", false, "", "", "", 0, 0, 0)
}