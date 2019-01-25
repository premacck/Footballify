package life.plank.juna.zone.ui.board.fragment.fixture

import android.os.Bundle
import android.view.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_board_info.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.model.football.*
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.util.view.UIDisplayUtil.getScreenSize
import life.plank.juna.zone.ui.base.fragment.BaseJunaFragment
import life.plank.juna.zone.ui.board.adapter.match.BoardMediaAdapter
import javax.inject.Inject

class MatchMediaFragment : BaseJunaFragment() {

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

    fun updateHighlights(highlightsList: List<Highlights>) {
        adapter?.updateHighlights(highlightsList, false)
    }

    override fun onDetach() {
        adapter = null
        super.onDetach()
    }
}