package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import kotlinx.android.parcel.*
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