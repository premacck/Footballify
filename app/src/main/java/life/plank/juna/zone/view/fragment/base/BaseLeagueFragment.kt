package life.plank.juna.zone.view.fragment.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.bumptech.glide.Glide
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.data.viewmodel.LeagueViewModel
import life.plank.juna.zone.interfaces.LeagueContainer
import life.plank.juna.zone.view.fragment.board.fixture.MatchBoardFragment

abstract class BaseLeagueFragment : FlatFragment(), LeagueContainer {

    protected lateinit var leagueViewModel: LeagueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueViewModel = ViewModelProviders.of(this).get(LeagueViewModel::class.java)
    }

    override fun getGlide() = Glide.with(this)

    override fun onFixtureSelected(matchFixture: MatchFixture, league: League) =
            pushFragment(MatchBoardFragment.newInstance(matchFixture, league))
}