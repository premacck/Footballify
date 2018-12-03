package life.plank.juna.zone.view.fragment.board.fixture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_board_info.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.AppConstants.HIGHLIGHTS_DATA
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.view.UIDisplayUtil.getScreenSize
import life.plank.juna.zone.view.adapter.board.match.BoardMediaAdapter
import life.plank.juna.zone.view.fragment.base.BaseFragment
import javax.inject.Inject

class MatchMediaFragment : BaseFragment() {

    @Inject
    lateinit var restApi: RestApi
    @Inject
    lateinit var gson: Gson

    lateinit var matchDetails: MatchDetails
    private var adapter: BoardMediaAdapter? = null

    companion object {
        fun newInstance(matchDetails: MatchDetails) = MatchMediaFragment().apply { arguments = Bundle().apply { putParcelable(findString(R.string.match_id_string), matchDetails) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        arguments?.run { matchDetails = getParcelable(getString(R.string.match_id_string))!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_board_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareRecyclerView()
    }

    private fun prepareRecyclerView() {
        adapter = BoardMediaAdapter(matchDetails, getScreenSize(activity)[0])
        list_board_info.adapter = adapter
    }

    fun updateZoneLiveData(zoneLiveData: ZoneLiveData) {
        when (zoneLiveData.liveDataType) {
            HIGHLIGHTS_DATA -> {
                val highlightsList = zoneLiveData.getHighlightsList(gson)
                if (highlightsList != null) {
                    adapter!!.updateHighlights(highlightsList, false)
                }
            }
        }
    }

    override fun onDetach() {
        adapter = null
        super.onDetach()
    }
}