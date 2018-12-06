package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class FixtureByMatchDay(
        var matchDay: Int = 0,
        var daySection: String = "",
        var fixtureByDateList: @RawValue MutableList<FixtureByDate> = Collections.emptyList()
) : Parcelable {
    constructor(matchDay: Int) : this(matchDay, "", ArrayList<FixtureByDate>())
}