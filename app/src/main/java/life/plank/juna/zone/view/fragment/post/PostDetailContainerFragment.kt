package life.plank.juna.zone.view.fragment.post

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_post_detail_container.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.util.facilis.floatUp
import java.lang.ref.WeakReference
import java.util.*

class PostDetailContainerFragment : BaseCard() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateViewPager()
        root_card.floatUp()
    }

    override fun getBackgroundBlurLayout(): ViewGroup? = blur_layout

    override fun getRootView(): CardView? = root_card

    override fun getDragView(): View? = drag_area

    private fun populateViewPager() {
        post_detail_view_pager.adapter = PostDetailPagerAdapter(childFragmentManager, this)
        post_detail_view_pager.currentItem = position
    }

    class PostDetailPagerAdapter(fm: FragmentManager, postDetailContainerFragment: PostDetailContainerFragment) : FragmentStatePagerAdapter(fm) {

        private val ref: WeakReference<PostDetailContainerFragment> = WeakReference(postDetailContainerFragment)

        override fun getItem(position: Int): Fragment? {
            return try {
                ref.get()?.run { PostDetailFragment.newInstance(feedList[position], boardId) }
            } catch (e: Exception) {
                null
            }
        }

        override fun getCount(): Int = ref.get()?.feedList?.size!!
    }
}
