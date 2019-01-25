package life.plank.juna.zone.ui.football

import com.bumptech.glide.RequestManager
import life.plank.juna.zone.data.model.football.*

interface LeagueContainer {

    fun getGlide(): RequestManager

    fun getTheLeague(): League

    fun onFixtureSelected(matchFixture: MatchFixture, league: League = getTheLeague())
}