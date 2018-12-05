package life.plank.juna.zone.view.fragment.football


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_league_info.*
import kotlinx.android.synthetic.main.league_toolbar.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FixtureByMatchDay
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.util.common.AppConstants.BoomMenuPage
import life.plank.juna.zone.util.common.DataUtil
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.util.view.UIDisplayUtil.findColor
import life.plank.juna.zone.util.view.setupBoomMenu

class LeagueInfoFragment : BaseCard() {

    private lateinit var league: League
    private var leagueInfoPagerAdapter: LeagueInfoFragment.LeagueInfoPagerAdapter? = null

    companion object {
        var fixtureByMatchDayList: MutableList<FixtureByMatchDay> = ArrayList()
        fun newInstance(league: League) = LeagueInfoFragment().apply { arguments = Bundle().apply { putParcelable(DataUtil.findString(R.string.intent_league), league) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply { league = getParcelable(getString(R.string.intent_league))!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_league_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBoomMenu(BoomMenuPage.BOOM_MENU_SETTINGS_AND_HOME, activity!!, null, arc_menu, null)

        prepareViewPager()
        league_info_tab_layout.setupWithViewPager(league_info_view_pager)

        title.text = league.name
        logo.setImageResource(league.leagueLogo)
        parent_layout.setBackgroundColor(findColor(league.dominantColor!!))
    }

    override fun getBackgroundBlurLayout(): ViewGroup? = null

    override fun getRootView(): CardView? = root_card

    override fun getDragView(): View? = drag_area

    private fun prepareViewPager() {
        leagueInfoPagerAdapter = LeagueInfoPagerAdapter(childFragmentManager, league)
        league_info_view_pager.adapter = leagueInfoPagerAdapter
    }

    fun setMatchday(matchday: Int) {
        league_matchday.text = getString(R.string.matchday_with_number, matchday)
    }

    fun hideOrShowBoomMenu(scrollY: Int, oldScrollY: Int) {
        if (scrollY > oldScrollY) {
            arc_menu.hide()
        } else {
            arc_menu.show()
        }
    }

    fun hideOrShowBoomMenu(dy: Int) {
        if (dy > 5) {
            arc_menu.hide()
        } else if (dy < -5) {
            arc_menu.show()
        }
    }

    override fun onDestroy() {
        leagueInfoPagerAdapter = null
        fixtureByMatchDayList.clear()
        super.onDestroy()
    }

    class LeagueInfoPagerAdapter(fm: FragmentManager?, private val league: League) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> FixtureFragment.newInstance(league)
                1 -> StandingsFragment.newInstance(league)
                2 -> LeagueStatsFragment.newInstance(league)
                else -> null
            }
        }

        override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> findString(R.string.fixtures_caps)
                1 -> findString(R.string.table_caps)
                2 -> findString(R.string.stats)
                else -> null
            }
        }
    }
}