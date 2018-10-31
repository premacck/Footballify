package life.plank.juna.zone.view.fragment.board.fixture


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_match_info.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.util.DataUtil
import life.plank.juna.zone.util.facilis.BaseCard
import java.lang.ref.WeakReference

class MatchInfoFragment : BaseCard() {

    private var currentMatchId: Long = 0
    private lateinit var league: League
    private var fixture: MatchFixture? = null
    private var infoPagerAdapter: MatchInfoFragment.InfoPagerAdapter? = null

    override fun getBackgroundBlurLayout(): ViewGroup? = null

    override fun getRootCard(): CardView? = info_root_card

    override fun getDragHandle(): View? = info_root_card

    companion object {
        private val TAG = MatchInfoFragment::class.java.simpleName
        fun newInstance(fixture: MatchFixture?, league: League): MatchInfoFragment = MatchInfoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(DataUtil.findString(R.string.intent_fixture_data), fixture)
                putParcelable(DataUtil.findString(R.string.intent_league), league)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_info, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)

        val intent = arguments!!
        if (intent.containsKey(getString(R.string.intent_fixture_data))) {
            fixture = intent.getParcelable(getString(R.string.intent_fixture_data))
            league = intent.getParcelable(getString(R.string.intent_league))!!
            currentMatchId = fixture!!.matchId
        } else {
            currentMatchId = intent.getLong(getString(R.string.match_id_string), 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        home_team.text = fixture?.homeTeam?.name
        visiting_team.text = fixture?.awayTeam?.name
        setupViewPagerWithFragments()
    }

    private fun setupViewPagerWithFragments() {
        infoPagerAdapter = MatchInfoFragment.InfoPagerAdapter(childFragmentManager, this)
        info_view_pager.adapter = infoPagerAdapter
        info_view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(info_tiles_tab_layout))
        info_tiles_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(info_view_pager))
    }

    class InfoPagerAdapter(supportFragmentManager: FragmentManager, matchInfoFragment: MatchInfoFragment) : FragmentStatePagerAdapter(supportFragmentManager) {

        var currentFragment: Fragment? = null
        private val ref: WeakReference<MatchInfoFragment> = WeakReference(matchInfoFragment)

        override fun getItem(position: Int): Fragment? {
            ref.get()?.run {
                return when (position) {
                    //TODO: Replace dummy fragment with required fragment
                    0 -> DummyFragment()
                    1 -> DummyFragment()
                    2 -> DummyFragment()
                    3 -> DummyFragment()
                    4 -> DummyFragment()
                    5 -> DummyFragment()
                    else -> {
                        null
                    }
                }
            }
            return null
        }

        override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

        override fun getCount(): Int = 5

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            if (currentFragment !== `object`) {
                currentFragment = `object` as Fragment
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }
}
