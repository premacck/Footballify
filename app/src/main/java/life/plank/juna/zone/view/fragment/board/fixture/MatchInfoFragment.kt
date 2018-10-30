package life.plank.juna.zone.view.fragment.board.fixture


import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_match_board.*
import kotlinx.android.synthetic.main.fragment_match_info.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.util.DataUtil
import life.plank.juna.zone.util.facilis.BaseCard

class MatchInfoFragment : BaseCard() {

    private var currentMatchId: Long = 0
    private lateinit var league: League
    private var fixture: MatchFixture? = null

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
    }
}
