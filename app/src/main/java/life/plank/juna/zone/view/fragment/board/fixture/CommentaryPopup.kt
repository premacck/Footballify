package life.plank.juna.zone.view.fragment.board.fixture

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_commentary.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Commentary
import life.plank.juna.zone.data.model.ZoneLiveData
import life.plank.juna.zone.util.AppConstants.COMMENTARY_DATA
import life.plank.juna.zone.util.DataUtil.*
import life.plank.juna.zone.util.facilis.floatUp
import life.plank.juna.zone.view.adapter.CommentaryAdapter
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup
import javax.inject.Inject

class CommentaryPopup : BaseBlurPopup() {

    @Inject
    lateinit var gson: Gson

    private var commentaryList: List<Commentary>? = null
    private var adapter: CommentaryAdapter? = null

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.hasExtra(getString(R.string.intent_zone_live_data))) {
                updateLiveCommentary(getZoneLiveData(intent, context.getString(R.string.intent_zone_live_data), gson)!!)
            }
        }
    }

    companion object {
        val Tag: String = CommentaryPopup::class.java.simpleName
        fun newInstance(commentaryList: ArrayList<Commentary>) = CommentaryPopup().apply { arguments = Bundle().apply { putParcelableArrayList(findString(R.string.commentary), commentaryList) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        commentaryList = arguments?.getParcelableArrayList(getString(R.string.commentary))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.popup_commentary, container, false)

    override fun doOnStart() {
        root_card.floatUp()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        context!!.registerReceiver(mMessageReceiver, IntentFilter(getString(R.string.intent_board)))
    }

    override fun onPause() {
        super.onPause()
        context!!.unregisterReceiver(mMessageReceiver)
    }

    override fun dismissAnimation(): Int = R.anim.sink_down

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout

    private fun initRecyclerView() {
        if (!isNullOrEmpty(commentaryList)) {
            adapter = CommentaryAdapter(commentaryList)
            (commentary_recycler_view.layoutManager as LinearLayoutManager).reverseLayout = true
            commentary_recycler_view.adapter = adapter
            commentary_recycler_view.scrollToPosition(commentaryList!!.size - 1)
        }
    }

    private fun updateLiveCommentary(zoneLiveData: ZoneLiveData) {
        if (zoneLiveData.liveDataType == COMMENTARY_DATA) {
            val commentaryList = zoneLiveData.getCommentaryList(gson)
            if (!isNullOrEmpty(commentaryList)) {
                adapter!!.updateNew(commentaryList)
                commentary_recycler_view.smoothScrollToPosition(adapter!!.commentaries.size + commentaryList!!.size - 1)
            }
        }
    }
}