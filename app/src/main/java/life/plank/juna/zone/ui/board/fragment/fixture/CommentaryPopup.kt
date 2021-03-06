package life.plank.juna.zone.ui.board.fragment.fixture

import android.os.Bundle
import android.view.*
import com.google.gson.Gson
import com.prembros.facilis.dialog.BaseBlurPopup
import com.prembros.facilis.util.isNullOrEmpty
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_commentary.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.football.Commentary
import life.plank.juna.zone.data.model.football.FootballLiveData
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.ui.board.adapter.match.CommentaryAdapter
import life.plank.juna.zone.util.common.AppConstants.COMMENTARY_DATA
import javax.inject.Inject

class CommentaryPopup : BaseBlurPopup() {

    @Inject
    lateinit var gson: Gson

    private lateinit var commentaryList: MutableList<Commentary>
    private var adapter: CommentaryAdapter? = null

    companion object {
        fun newInstance(commentaryList: ArrayList<Commentary>) = CommentaryPopup().apply { arguments = Bundle().apply { putParcelableArrayList(findString(R.string.commentary), commentaryList) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.application.uiComponent.inject(this)
        commentaryList = arguments?.getParcelableArrayList(getString(R.string.commentary))!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.popup_commentary, container, false)

    override fun doOnStart() {
        initRecyclerView()
    }

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout

    private fun initRecyclerView() {
        if (!isNullOrEmpty(commentaryList)) {
            adapter = CommentaryAdapter(commentaryList)
            (commentary_recycler_view.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).reverseLayout = true
            commentary_recycler_view.adapter = adapter
            commentary_recycler_view.scrollToPosition(commentaryList.size - 1)
        }
    }

    private fun updateLiveCommentary(footballLiveData: FootballLiveData) {
        if (footballLiveData.liveDataType == COMMENTARY_DATA) {
            (footballLiveData.getCommentaryList(gson) as? MutableList)?.run {
                if (!isNullOrEmpty(this)) {
                    adapter?.updateNew(this)
                    commentary_recycler_view.smoothScrollToPosition(adapter!!.commentaries.size + size - 1)
                }
            }
        }
    }
}