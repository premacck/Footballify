package life.plank.juna.zone.interfaces

import com.bumptech.glide.RequestManager
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.MatchFixture

interface LeagueContainer {

    fun getGlide(): RequestManager

    fun getTheLeague(): League

    fun onFixtureSelected(matchFixture: MatchFixture, league: League = getTheLeague())
}