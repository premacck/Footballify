package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class FixtureByDate(
        var date: Date = Date(),
        var fixtures: @RawValue MutableList<MatchFixture> = Collections.emptyList()
) : Parcelable

fun FixtureByDate.put(date: Date, fixture: MatchFixture): FixtureByDate {
    this.date = date
    this.fixtures = ArrayList()
    fixtures.add(fixture)
    return this
}