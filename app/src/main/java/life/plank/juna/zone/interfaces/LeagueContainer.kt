package life.plank.juna.zone.interfaces

import com.bumptech.glide.RequestManager
import com.google.gson.Gson
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.MatchFixture

interface LeagueContainer {

    fun getGlide(): RequestManager

    fun getTheGson(): Gson

    fun getTheLeague(): League

    fun onFixtureSelected(matchFixture: MatchFixture, league: League = getTheLeague())
}