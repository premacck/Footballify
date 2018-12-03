package life.plank.juna.zone.data.local.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import life.plank.juna.zone.data.model.*
import java.util.Collections.emptyList

@Entity
@Parcelize
data class LeagueInfo @Ignore constructor(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @Embedded(prefix = "league_") var league: League = League(),
        var fixtureByMatchDayList: @RawValue List<FixtureByMatchDay> = emptyList(),
        var standingsList: @RawValue List<Standings> = emptyList(),
        var teamStatsList: @RawValue List<TeamStats> = emptyList(),
        var playerStatsList: @RawValue List<PlayerStats> = emptyList()
) : Parcelable {
    constructor() : this(0, League(), emptyList(), emptyList(), emptyList(), emptyList())

    constructor(league: League, fixtureList: MutableList<FixtureByMatchDay>) : this(0, league, fixtureList)
}