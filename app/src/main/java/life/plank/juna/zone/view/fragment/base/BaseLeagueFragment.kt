package life.plank.juna.zone.view.fragment.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.Glide
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.data.viewmodel.LeagueViewModel
import life.plank.juna.zone.interfaces.LeagueContainer
import life.plank.juna.zone.view.fragment.board.fixture.MatchBoardFragment
import life.plank.juna.zone.view.fragment.football.LeagueInfoFragment

abstract class BaseLeagueFragment : FlatFragment(), LeagueContainer {

    protected lateinit var leagueViewModel: LeagueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueViewModel = ViewModelProviders.of(this).get(LeagueViewModel::class.java)
    }

    override fun getGlide() = Glide.with(this)

    override fun onFixtureSelected(matchFixture: MatchFixture, league: League) =
            pushFragment(MatchBoardFragment.newInstance(matchFixture, league))

    protected fun NestedScrollView.setupWithParentFragmentBoomMenu() {
        setOnScrollChangeListener { _, _, scrollY, _, oldScrollY -> (parentFragment as? LeagueInfoFragment)?.hideOrShowBoomMenu(scrollY, oldScrollY) }
    }

    protected fun RecyclerView.setupWithParentFragmentBoomMenu() {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                (parentFragment as? LeagueInfoFragment)?.hideOrShowBoomMenu(dy)
            }
        })
    }
}