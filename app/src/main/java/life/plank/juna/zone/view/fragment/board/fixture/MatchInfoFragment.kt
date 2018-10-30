package life.plank.juna.zone.view.fragment.board.fixture


import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_match_board.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.model.MatchFixture
import life.plank.juna.zone.util.DataUtil
import life.plank.juna.zone.util.facilis.BaseCard

class MatchInfoFragment : BaseCard() {

    override fun getBackgroundBlurLayout(): ViewGroup? = null

    override fun getRootCard(): CardView? = root_card

    override fun getDragHandle(): View? = drag_area

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


}
