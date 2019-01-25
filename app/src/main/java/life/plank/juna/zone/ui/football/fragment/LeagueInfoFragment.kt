package life.plank.juna.zone.ui.football.fragment


import android.os.Bundle
import android.view.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.*
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.fragment_league_info.*
import kotlinx.android.synthetic.main.league_toolbar.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.football.*
import life.plank.juna.zone.service.CommonDataService
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.util.common.AppConstants.BoomMenuPage
import life.plank.juna.zone.util.view.*
import life.plank.juna.zone.util.view.UIDisplayUtil.findColor
import life.plank.juna.zone.ui.base.fragment.BaseJunaCard

class LeagueInfoFragment : BaseJunaCard() {

    private lateinit var league: League
    private var leagueInfoPagerAdapter: LeagueInfoPagerAdapter? = null

    companion object {
        var fixtureByMatchDayList: MutableList<FixtureByMatchDay> = ArrayList()
        fun newInstance(league: League) = LeagueInfoFragment().apply { arguments = Bundle().apply { putParcelable(CommonDataService.findString(R.string.intent_league), league) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply { league = getParcelable(getString(R.string.intent_league))!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_league_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBoomMenu(BoomMenuPage.BOOM_MENU_SETTINGS_AND_HOME, activity!!, null, null)

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
            boomMenu().hide()
        } else {
            boomMenu().show()
        }
    }

    fun hideOrShowBoomMenu(dy: Int) {
        if (dy > 5) {
            boomMenu().hide()
        } else if (dy < -5) {
            boomMenu().show()
        }
    }

    override fun onDestroy() {
        leagueInfoPagerAdapter = null
        fixtureByMatchDayList.clear()
        super.onDestroy()
    }

    class LeagueInfoPagerAdapter(fm: FragmentManager, private val league: League) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> FixtureFragment.newInstance(league)
                1 -> StandingsFragment.newInstance(league)
                2 -> LeagueStatsFragment.newInstance(league)
                else -> LeagueStatsFragment.newInstance(league)
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