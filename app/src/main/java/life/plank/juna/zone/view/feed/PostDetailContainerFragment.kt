package life.plank.juna.zone.view.feed

import android.os.Bundle
import android.view.*
import androidx.viewpager.widget.ViewPager
import com.prembros.facilis.fragment.*
import kotlinx.android.synthetic.main.fragment_post_detail_container.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.feed.FeedEntry
import life.plank.juna.zone.service.CommonDataService.findString
import java.util.*

class PostDetailContainerFragment : BaseCardListContainerFragment() {

    private lateinit var feedList: List<FeedEntry>
    private lateinit var boardId: String
    private var position: Int = 0

    companion object {
        val TAG: String = PostDetailContainerFragment::class.java.simpleName
        fun newInstance(feedEntryList: List<FeedEntry>, boardId: String, position: Int) = PostDetailContainerFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(findString(R.string.intent_feed_items), feedEntryList as ArrayList<FeedEntry>)
                putString(findString(R.string.intent_board_id), boardId)
                putInt(findString(R.string.intent_position), position)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            feedList = getParcelableArrayList(getString(R.string.intent_feed_items))!!
            boardId = getString(getString(R.string.intent_board_id))!!
            position = getInt(getString(R.string.intent_position), 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_post_detail_container, container, false)

    override fun backgroundBlurLayout(): ViewGroup? = blur_layout

    override fun initialPosition(): Int = position

    override fun baseCardCount(): Int = feedList.size

    override fun viewPager(): ViewPager = post_detail_view_pager

    override fun baseCardToInflate(position: Int): BaseCardFragment = PostDetailFragment.newInstance(feedList[position], boardId)
}
