package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import kotlinx.android.parcel.*
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