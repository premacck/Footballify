package life.plank.juna.zone.data.model.football

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.*
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