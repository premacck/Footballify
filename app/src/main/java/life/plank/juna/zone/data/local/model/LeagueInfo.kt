package life.plank.juna.zone.data.local.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import life.plank.juna.zone.data.model.*

@Entity
@Parcelize
data class LeagueInfo(
        @PrimaryKey @Embedded(prefix = "league_") var league: League,
        var fixtureByMatchDayList: @RawValue List<FixtureByMatchDay>,
        var standingsList: @RawValue List<Standings>,
        var teamStatsList: @RawValue List<TeamStats>,
        var playerStatsList: @RawValue List<PlayerStats>
) : Parcelable