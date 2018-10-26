package life.plank.juna.zone.view.fragment.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.data.viewmodel.LeagueViewModel
import life.plank.juna.zone.interfaces.LeagueContainer
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.view.fragment.board.fixture.MatchBoardFragment

abstract class BaseLeagueFragment : BaseCard(), LeagueContainer {

    protected lateinit var leagueViewModel: LeagueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueViewModel = ViewModelProviders.of(this).get(LeagueViewModel::class.java)
    }

    override fun onFixtureSelected(matchFixture: MatchFixture, league: League) {
        pushFragment(MatchBoardFragment.newInstance(matchFixture, league), true)
    }
}