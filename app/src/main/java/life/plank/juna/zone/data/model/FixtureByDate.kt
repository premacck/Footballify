package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Parcelize
data class FixtureByDate(
        var date: Date,
        var fixtures: @RawValue List<MatchFixture>
) : Parcelable