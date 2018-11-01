package life.plank.juna.zone.view.fragment.board.fixture


import android.content.*
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.CardView
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_match_info.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.RestApiAggregator
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.AppConstants
import life.plank.juna.zone.util.DataUtil
import life.plank.juna.zone.util.FixtureListUpdateTask
import life.plank.juna.zone.util.UIDisplayUtil
import life.plank.juna.zone.util.facilis.BaseCard
import rx.Subscriber
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Named

class MatchInfoFragment : BaseCard() {

    @Inject
    lateinit var gson: Gson
    @field: [Inject Named("default")]
    lateinit var restApi: RestApi
    @field: [Inject Named("footballData")]
    lateinit var footballRestApi: RestApi
    @Inject
    lateinit var picasso: Picasso
    private var matchDetails: MatchDetails? = null
    private var boardId: String? = null
    private var currentMatchId: Long = 0
    private lateinit var league: League
    private var fixture: MatchFixture? = null
    private var infoPagerAdapter: MatchInfoFragment.InfoPagerAdapter? = null
    private var isBoardActive: Boolean = false

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

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.hasExtra(getString(R.string.intent_zone_live_data))) {
                setZoneLiveData(intent)
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
        getBoardIdAndMatchDetails(currentMatchId)
    }

    private fun setupViewPagerWithFragments() {
        infoPagerAdapter = MatchInfoFragment.InfoPagerAdapter(childFragmentManager, this)
        info_view_pager.adapter = infoPagerAdapter
        info_view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(info_tiles_tab_layout))
        info_tiles_tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(info_view_pager))
    }

    override fun onResume() {
        super.onResume()
        context?.registerReceiver(mMessageReceiver, IntentFilter(getString(R.string.intent_board)))
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(mMessageReceiver)
    }

    private fun setZoneLiveData(intent: Intent) {
        val zoneLiveData = DataUtil.getZoneLiveData(intent, getString(R.string.intent_zone_live_data), gson)
        when (zoneLiveData!!.liveDataType) {
            AppConstants.SCORE_DATA -> {
                val scoreData = zoneLiveData.getScoreData(gson)
                DataUtil.updateScoreLocally(fixture!!, scoreData)
                DataUtil.updateScoreLocally(matchDetails!!, scoreData)

                FixtureListUpdateTask.update(fixture, scoreData, null, true)
            }
            AppConstants.TIME_STATUS_DATA -> {
                val timeStatus = zoneLiveData.getLiveTimeStatus(gson)
                DataUtil.updateTimeStatusLocally(fixture!!, timeStatus)
                DataUtil.updateTimeStatusLocally(matchDetails!!, timeStatus)
                FixtureListUpdateTask.update(fixture, null, timeStatus, false)
            }
            AppConstants.BOARD_ACTIVATED -> {
                isBoardActive = true
                setupViewPagerWithFragments()
            }
            AppConstants.BOARD_DEACTIVATED -> {
                isBoardActive = false
                UIDisplayUtil.showBoardExpirationDialog(activity) { dialog, _ ->
                    setupViewPagerWithFragments()
                    dialog.cancel()
                }
            }
        }
        try {
            if (infoPagerAdapter?.currentFragment is LineupFragment) {
                (infoPagerAdapter?.currentFragment as? LineupFragment)?.updateZoneLiveData(zoneLiveData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getBoardIdAndMatchDetails(currentMatchId: Long?) {
        RestApiAggregator.getBoardAndMatchDetails(restApi, footballRestApi, currentMatchId!!)
                .doOnSubscribe { info_progress_bar!!.visibility = View.VISIBLE }
                .doOnTerminate { info_progress_bar!!.visibility = View.GONE }
                .subscribe(object : Subscriber<Pair<Board, MatchDetails>>() {
                    override fun onCompleted() {
                        Log.i(MatchInfoFragment.TAG, "onCompleted: getBoardIdAndMatchDetails")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(MatchInfoFragment.TAG, "In onError() : getBoardIdAndMatchDetails$e")
                        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
                    }

                    override fun onNext(boardMatchDetailsPair: Pair<Board, MatchDetails>?) {
                        if (boardMatchDetailsPair != null) {
                            matchDetails = boardMatchDetailsPair.second
                            val board = boardMatchDetailsPair.first
                            if (matchDetails != null) {
                                matchDetails!!.league = league
                            }
                            if (board != null) {
                                boardId = board.id
                                saveBoardId()
                                isBoardActive = board.isActive!!

                                if (isBoardActive) {
                                    FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.board_id_prefix) + boardId!!)
                                    FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.pref_football_match_sub) + currentMatchId)
                                }
                            }
                            setupViewPagerWithFragments()
                        } else {
                            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    fun saveBoardId() {
        val boardIdEditor: SharedPreferences.Editor = activity?.getSharedPreferences(getString(R.string.pref_enter_board_id), Context.MODE_PRIVATE)!!.edit()
        boardIdEditor.putString(getString(R.string.pref_enter_board_id), boardId).apply()
    }

    class InfoPagerAdapter(supportFragmentManager: FragmentManager, matchInfoFragment: MatchInfoFragment) : FragmentStatePagerAdapter(supportFragmentManager) {

        var currentFragment: Fragment? = null
        private val ref: WeakReference<MatchInfoFragment> = WeakReference(matchInfoFragment)

        override fun getItem(position: Int): Fragment? {
            ref.get()?.run {
                return when (position) {
                    //TODO: Replace dummy fragment with required fragment
                    0 -> PrematchInfoFragment()
                    1 -> LineupFragment.newInstance(gson.toJson(matchDetails))
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
