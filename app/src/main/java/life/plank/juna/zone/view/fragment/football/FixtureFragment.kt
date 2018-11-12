package life.plank.juna.zone.view.fragment.football

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.fragment_fixture.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.interfaces.LeagueContainer
import life.plank.juna.zone.util.AppConstants.PAST_MATCHES
import life.plank.juna.zone.util.AppConstants.TODAY_MATCHES
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.facilis.fadeOut
import life.plank.juna.zone.view.adapter.FixtureMatchdayAdapter
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup
import life.plank.juna.zone.view.fragment.football.LeagueInfoFragment.Companion.fixtureByMatchDayList
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

class FixtureFragment : BaseBlurPopup(), LeagueContainer {

    @Inject
    lateinit var gson: Gson
    private lateinit var league: League

    private var fixtureMatchdayAdapter: FixtureMatchdayAdapter? = null

    companion object {
        val TAG: String = FixtureFragment::class.java.simpleName
        fun newInstance(league: League) = FixtureFragment().apply { arguments = Bundle().apply { putParcelable(findString(R.string.intent_league), league) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)

        if (arguments == null) {
            onNoMatchesFound()
            return
        }
        league = arguments?.getParcelable(getString(R.string.intent_league))!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_fixture, container, false)

    override fun onStart() {
        fixtureMatchdayAdapter = FixtureMatchdayAdapter(this)
        fixtures_section_list.adapter = fixtureMatchdayAdapter
        super.onStart()
    }

    override fun doOnStart() {
        blur_layout.visibility = VISIBLE
        root_card.visibility = VISIBLE
        updateFixtureAdapter()
    }

    override fun doOnStop() {
        fixtureMatchdayAdapter = null
    }

    override fun doOnDismiss() {
        fixtures_section_list?.fadeOut()
    }

    override fun getBlurLayout(): BlurLayout? = blur_layout

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_layout

    override fun getDragHandle(): View? = drag_area

    override fun getGlide() = Glide.with(activity!!)

    override fun getTheGson() = gson

    override fun getTheLeague() = league

    override fun onFixtureSelected(matchFixture: MatchFixture, league: League) {
        getParentActivity()?.openBoardFromFixtureList(matchFixture, league)
    }

    private fun onNoMatchesFound() {
        no_data.visibility = View.VISIBLE
        fixtures_section_list.visibility = View.GONE
    }

    private fun updateFixtureAdapter() {
        progress_bar.visibility = View.VISIBLE
        doAsync {
            var recyclerViewScrollIndex = 0
            if (!isNullOrEmpty(fixtureByMatchDayList)) {
                for (matchDay in fixtureByMatchDayList) {
                    if (matchDay.daySection == PAST_MATCHES || matchDay.daySection == TODAY_MATCHES) {
                        recyclerViewScrollIndex = fixtureByMatchDayList.indexOf(matchDay)
                    }
                }
            }
            uiThread {
                progress_bar.visibility = View.GONE
                fixtures_section_list.scrollToPosition(recyclerViewScrollIndex)
            }
        }
    }
}