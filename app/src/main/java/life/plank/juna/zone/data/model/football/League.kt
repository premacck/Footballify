package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.room.*
import com.google.gson.annotations.*
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
        @SerializedName("foreignId") @Expose var id: Long = 0
) : Parcelable {
    @Ignore constructor(id: Long, name: String, isCup: Boolean, seasonName: String?, countryName: String?, thumbUrl: String?, dominantColor: Int?, leagueLogo: Int) :
            this(name, isCup, seasonName, countryName, thumbUrl, dominantColor, leagueLogo, id)

    constructor() : this("", false, "", "", "", 0, 0, 0)
}