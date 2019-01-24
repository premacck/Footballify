package life.plank.juna.zone.view.base.fragment

import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import life.plank.juna.zone.component.helper.launchMatchBoard
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.model.football.*
import life.plank.juna.zone.data.viewmodel.LeagueViewModel
import life.plank.juna.zone.view.football.LeagueContainer
import life.plank.juna.zone.view.football.fragment.LeagueInfoFragment

abstract class BaseLeagueFragment : FlatFragment(), LeagueContainer {

    protected lateinit var leagueViewModel: LeagueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueViewModel = ViewModelProviders.of(this).get(LeagueViewModel::class.java)
    }

    override fun getGlide() = Glide.with(this)

    override fun onFixtureSelected(matchFixture: MatchFixture, league: League) =
            getParentActivity().launchMatchBoard(restApi(), matchFixture.matchId)

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

    abstract fun restApi(): RestApi
}